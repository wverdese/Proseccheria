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
                    it.toList()
                } // put all the TableData.Items in a list
                    .map { items ->
                        TableData(table, items)
                    } // finally, wrap up the TableData
            }

    fun selectTable(tableId: TableId) {
        tables.find { it.id == tableId }?.also { table -> _selectedTable.update { table } }
    }

    suspend fun update(tableId: TableId, item: TableData.Item) {
        val itemData = item.asItemData(tableId)
        itemDataStore.putString(itemData.id, itemData.serialize())
    }
}
