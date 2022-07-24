package me.wverdese.proseccheria.repo

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.wverdese.proseccheria.Cancellable
import me.wverdese.proseccheria.collect
import me.wverdese.proseccheria.converter.asItem
import me.wverdese.proseccheria.converter.asItemData
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.BOTTLE
import me.wverdese.proseccheria.model.GLASS
import me.wverdese.proseccheria.model.ItemData
import me.wverdese.proseccheria.model.Menu
import me.wverdese.proseccheria.model.NotesType
import me.wverdese.proseccheria.model.QuantityType
import me.wverdese.proseccheria.model.Table
import me.wverdese.proseccheria.model.TableId
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSettingsApi::class)
class TableDataRepository(
    private val tables: List<Table> = me.wverdese.proseccheria.model.tables,
    private val menu: Menu = me.wverdese.proseccheria.model.menu,
) : KoinComponent {
    val scope = CoroutineScope(Dispatchers.Main)

    private val itemDataStore: FlowSettings = get()

    private val _selectedTable = MutableStateFlow(tables.first())
    val selectedTable: StateFlow<Table> = _selectedTable

    private val itemComparator = compareBy<TableData.Item> { it.item.type.ordinal }.thenBy { it.item.name }

    val observeTableData: Flow<TableData> =
        // update when a table is selected
        selectedTable
            .flatMapLatest { table ->
                combine(
                    // convert each menuItem in a Flow<TableData.Item>
                    menu.map { item ->
                        itemDataStore
                            .getStringOrNullFlow(
                                ItemData.id(table.id, item.id)
                            ) // fetch json and update when it's changed (thanks to flatMapLatest)
                            .map { json ->
                                json?.let { ItemData.parse(it) }.asItem(item)
                            } // parse stored json into a TableData.Item
                    }
                ) {
                    it.sortedWith(itemComparator)
                } // put all the TableData.Items in a sorted list
                    .map { items ->
                        val tableItems = tables.map { table ->
                            TableData.TableItem(
                                table = table,
                                hasStoredData = itemDataStore.keys().find { key -> key.startsWith(table.id) } != null
                            )
                        } // compute hasStoredData information for TableItems
                        TableData(tableItems, table, items)
                    } // finally, wrap up the TableData
            }

    @Suppress("unused") // Called in iOS.
    fun observeTableDataIos(onEach: (TableData) -> Unit): Cancellable =
        observeTableData.collect(scope = scope, onEach = onEach) {
            println("An error has occurred.")
            it?.printStackTrace()
        }

    fun selectTable(tableId: TableId) {
        tables.find { it.id == tableId }?.also { table -> _selectedTable.update { table } }
    }

    suspend fun update(tableId: TableId, item: TableData.Item) {
        update(item.asItemData(tableId))
    }

    suspend fun updateNotes(tableId: TableId, item: TableData.Item, notes: NotesType?) {
        val data = item.asItemData(tableId)
        updateIfNeeded(data, data.copy(notes = notes))
    }

    suspend fun incrementQuantity(tableId: TableId, item: TableData.Item) =
        updateQuantity(tableId, item) { plus(1) }

    @Suppress("unused") // Called in iOS.
    fun incrementQuantityIos(tableId: TableId, item: TableData.Item) =
        scope.launch {
            incrementQuantity(tableId = tableId, item = item)
        }

    suspend fun decrementQuantity(tableId: TableId, item: TableData.Item) =
        updateQuantity(tableId, item) { minus(1) }

    private suspend fun updateQuantity(
        tableId: TableId,
        item: TableData.Item,
        update: QuantityType.() -> QuantityType
    ) {
        val coercedQuantity = item.quantity.update().coerceIn(0, 99)
        val data = item.asItemData(tableId)
        updateIfNeeded(data, data.copy(quantity = coercedQuantity))
    }

    suspend fun changeVessel(tableId: TableId, wineItem: TableData.Item.WineItem) {
        if (wineItem.canChangeVessel) {
            val data = wineItem.asItemData(tableId)
            updateIfNeeded(data, data.copy(vessel = if (wineItem.vessel == GLASS) BOTTLE else GLASS))
        }
    }

    suspend fun clear(tableId: TableId) {
        itemDataStore
            .keys()
            .filter {
                it.startsWith(tableId)
            }
            .forEach {
                itemDataStore.remove(it)
            }
    }


    private suspend fun updateIfNeeded(itemData: ItemData, newItemData: ItemData) {
        if (itemData != newItemData) {
            update(newItemData)
        }
    }

    private suspend fun update(itemData: ItemData) {
        itemDataStore.putString(itemData.id, itemData.serialize())
    }
}
