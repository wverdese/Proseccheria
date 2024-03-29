package me.wverdese.proseccheria.repo

import app.cash.turbine.test
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.MockSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.wverdese.proseccheria.converter.asItemData
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.BOTTLE
import me.wverdese.proseccheria.model.Food
import me.wverdese.proseccheria.model.GLASS
import me.wverdese.proseccheria.model.ItemData
import me.wverdese.proseccheria.model.Other
import me.wverdese.proseccheria.model.Wine
import me.wverdese.proseccheria.model.createTables
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@ExperimentalSettingsApi
@ExperimentalCoroutinesApi
class TableDataRepositoryTest {
    private val testCoroutineDispatcher = UnconfinedTestDispatcher()

    private val tables = createTables(2)
    private val food = Food(id = "FA-01", type = Food.Type.Antipasto, name = "Tagliere salumi e formaggi")
    private val wine =
        Wine(id = "WP-01", type = Wine.Type.Prosecco, vessel = Wine.Vessel.BOTH, name = "Valdobbiadene Ex Dry")
    private val other = Other(id = "SC-01", type = Other.Type.Spirit, name = "Aperol Spritz")
    private val menu = listOf(food, wine, other)

    private val mockItemDataStore = MockSettings().toFlowSettings(testCoroutineDispatcher)
    private lateinit var repo: TableDataRepository

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    single { mockItemDataStore }
                }
            )
        }
        repo = TableDataRepository(tables = tables, menu = menu)
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test select table`() = runTest(context = testCoroutineDispatcher) {
        repo.selectedTable.test {
            // init check
            assertEquals("T-01", awaitItem().id)
            // update check
            repo.selectTable("T-02")
            assertEquals("T-02", awaitItem().id)
        }
    }

    @Test
    fun `test update food item data`() = runTest(context = testCoroutineDispatcher) {
        val table = tables.first()
        val id = ItemData.id(table.id, food.id)

        val foodItem = TableData.Item.FoodItem(item = food, quantity = 2, notes = "test")

        mockItemDataStore.getStringOrNullFlow(id).test {
            // init check
            assertNull(awaitItem())
            // update check
            repo.update(table.id, foodItem)
            val json = awaitItem()
            assertNotNull(json)
            val itemData = ItemData.parse(json)
            assertEquals(foodItem.asItemData(table.id), itemData)
            assertEquals(table.id, itemData.tableId)
            assertEquals(foodItem.item.id, itemData.menuItemId)
            assertEquals(2, itemData.quantity)
            assertEquals("test", itemData.notes)
            assertNull(itemData.vessel)
        }
    }

    @Test
    fun `test update other item data`() = runTest(context = testCoroutineDispatcher) {
        val table = tables.first()
        val id = ItemData.id(table.id, other.id)

        val otherItem = TableData.Item.OtherItem(item = other, quantity = 2, notes = "test")

        mockItemDataStore.getStringOrNullFlow(id).test {
            // init check
            assertNull(awaitItem())
            //update check
            repo.update(table.id, otherItem)
            val json = awaitItem()
            assertNotNull(json)
            val itemData = ItemData.parse(json)
            assertEquals(otherItem.asItemData(table.id), itemData)
            assertEquals(table.id, itemData.tableId)
            assertEquals(otherItem.item.id, itemData.menuItemId)
            assertEquals(2, itemData.quantity)
            assertEquals("test", itemData.notes)
            assertNull(itemData.vessel)
        }
    }

    @Test
    fun `test update wine item data`() = runTest(context = testCoroutineDispatcher) {
        val table = tables.first()
        val id = ItemData.id(table.id, wine.id)

        val wineItem = TableData.Item.WineItem(item = wine, quantity = 2, notes = "test", vessel = BOTTLE)

        mockItemDataStore.getStringOrNullFlow(id).test {
            // init check
            assertNull(awaitItem())
            // update check
            repo.update(table.id, wineItem)
            var json = awaitItem()
            assertNotNull(json)
            var itemData = ItemData.parse(json)
            assertEquals(wineItem.asItemData(table.id), itemData)
            assertEquals(table.id, itemData.tableId)
            assertEquals(wineItem.item.id, itemData.menuItemId)
            assertEquals(2, itemData.quantity)
            assertEquals("test", itemData.notes)
            assertEquals(BOTTLE, itemData.vessel)
            // null check quantity
            repo.update(table.id, wineItem.copy(quantity = null))
            json = awaitItem()
            assertNotNull(json)
            itemData = ItemData.parse(json)
            assertNull(itemData.quantity)
            // null check notes
            repo.update(table.id, wineItem.copy(notes = null))
            json = awaitItem()
            assertNotNull(json)
            itemData = ItemData.parse(json)
            assertNull(itemData.notes)
            // null check vessel
            repo.update(table.id, wineItem.copy(vessel = null))
            json = awaitItem()
            assertNotNull(json)
            itemData = ItemData.parse(json)
            assertNull(itemData.vessel)
        }
    }

    @Test
    fun `test update item`() = runTest(context = testCoroutineDispatcher) {
        val tableId = tables.first().id
        val otherTableId = tables.last().id
        val wineItem = TableData.Item.WineItem(wine, quantity = null, notes = null, vessel = null)
        val wineItemUpdated = TableData.Item.WineItem(wine, quantity = 2, notes = "test", vessel = GLASS)

        repo.observeTableData.test {
            // initial check
            var tableData = awaitItem()
            assertEquals(tableId, tableData.table.id)
            assertEquals(wineItem, tableData.items.find { it.item == wine })
            // update check
            repo.update(tableId, wineItemUpdated)
            tableData = awaitItem()
            assertEquals(tableId, tableData.table.id)
            assertEquals(wineItemUpdated, tableData.items.find { it.item == wine })
            // table change
            repo.selectTable(otherTableId)
            tableData = awaitItem()
            assertEquals(otherTableId, tableData.table.id)
            assertEquals(wineItem, tableData.items.find { it.item == wine })
            // table reset
            repo.selectTable(tableId)
            tableData = awaitItem()
            assertEquals(tableId, tableData.table.id)
            assertEquals(wineItemUpdated, tableData.items.find { it.item == wine })
        }
    }

    @Test
    fun `test clear`() = runTest(context = testCoroutineDispatcher) {
        val tableId = tables.first().id
        val foodItem = TableData.Item.FoodItem(food, quantity = 2, notes = "test")
        val wineItem = TableData.Item.WineItem(wine, quantity = 2, notes = "test", vessel = GLASS)
        val otherItem = TableData.Item.OtherItem(other, quantity = 2, notes = "test")
        val foodItemCleared = TableData.Item.FoodItem(food, quantity = null, notes = null)
        val wineItemCleared = TableData.Item.WineItem(wine, quantity = null, notes = null, vessel = null)
        val otherItemCleared = TableData.Item.OtherItem(other, quantity = null, notes = null)

        repo.observeTableData.test {
            // update check
            repo.selectTable(tableId)
            repo.update(tableId, foodItem)
            repo.update(tableId, wineItem)
            repo.update(tableId, otherItem)
            skipItems(3)
            var tableData = awaitItem()
            assertEquals(tableId, tableData.table.id)
            assertEquals(foodItem, tableData.items.find { it.item == food })
            assertEquals(wineItem, tableData.items.find { it.item == wine })
            assertEquals(otherItem, tableData.items.find { it.item == other })
            // clear check
            repo.clear(tableId)
            skipItems(2)
            tableData = awaitItem()
            assertEquals(foodItemCleared, tableData.items.find { it.item == food })
            assertEquals(wineItemCleared, tableData.items.find { it.item == wine })
            assertEquals(otherItemCleared, tableData.items.find { it.item == other })
        }
    }
}
