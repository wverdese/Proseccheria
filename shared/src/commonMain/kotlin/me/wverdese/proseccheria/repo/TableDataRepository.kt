package me.wverdese.proseccheria.repo

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import me.wverdese.proseccheria.converter.asItem
import me.wverdese.proseccheria.converter.asItemData
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.ItemData
import me.wverdese.proseccheria.model.Menu
import me.wverdese.proseccheria.model.Table
import me.wverdese.proseccheria.model.TableId
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@ExperimentalCoroutinesApi
@ExperimentalSettingsApi
class TableDataRepository(
    private val tables: List<Table> = me.wverdese.proseccheria.model.tables,
    private val menu: Menu = me.wverdese.proseccheria.model.menu,
) : KoinComponent {
    private val itemDataStore: FlowSettings = get()

    private val _selectedTable = MutableStateFlow(tables.first())
    val selectedTable: StateFlow<Table> = _selectedTable

    val observeTableData: Flow<TableData> =
        selectedTable
            .flatMapLatest { table ->
                combine(
                    menu.map { item ->
                        itemDataStore
                            .getStringOrNullFlow(ItemData.id(table.id, item.id))
                            .map { json -> json?.let { ItemData.parse(it) }.asItem(item) }

                    }
                ) {
                    it.toList()
                }
                    .map { items -> TableData(table, items) }
            }

    fun selectTable(tableId: TableId) {
        tables.find { it.id == tableId }?.also { table -> _selectedTable.update { table } }
    }

    suspend fun update(tableId: TableId, item: TableData.Item) {
        val itemData = item.asItemData(tableId)
        itemDataStore.putString(itemData.id, itemData.serialize())
    }
}
