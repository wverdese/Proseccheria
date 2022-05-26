package me.wverdese.proseccheria.android.screen.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.NotesType
import me.wverdese.proseccheria.model.Table
import me.wverdese.proseccheria.repo.TableDataRepository

class OrderScreenViewModel(
    private val tableDataRepo: TableDataRepository
) : ViewModel() {
    var state: OrderScreenState by mutableStateOf(initScreenState())
        private set

    init {
        viewModelScope.launch {
            tableDataRepo.observeTableData.collect { data ->
                state = state.copy(
                    table = data.table,
                    isClearTableButtonEnabled = data.hasStoredData,
                    isViewOrderButtonEnabled = data.hasOrders,
                    groupedItems = data.items
                        .groupBy { it.item.type.name }
                        .mapValues { (_, list) -> list.sortedBy { it.item.name } }
                )
            }
        }
    }

    fun selectTable(table: Table) {
        tableDataRepo.selectTable(table.id)
    }

    fun editNotes(item: TableData.Item, notes: NotesType?) {
        viewModelScope.launch {
            tableDataRepo.updateNotes(state.table.id, item, notes)
        }
    }

    fun incrementQuantity(item: TableData.Item) {
        viewModelScope.launch {
            tableDataRepo.incrementQuantity(state.table.id, item)
        }
    }

    fun decrementQuantity(item: TableData.Item) {
        viewModelScope.launch {
            tableDataRepo.decrementQuantity(state.table.id, item)
        }
    }

    fun changeVessel(item: TableData.Item.WineItem) {
        viewModelScope.launch {
            tableDataRepo.changeVessel(state.table.id, item)
        }
    }

    fun clearTable(table: Table) {
        viewModelScope.launch {
            tableDataRepo.clear(table.id)
        }
    }

    private fun initScreenState() = OrderScreenState(
        tables = tableDataRepo.tables,
        table = tableDataRepo.tables.first(),
        isClearTableButtonEnabled = false,
        isViewOrderButtonEnabled = false,
        groupedItems = emptyMap(),
    )
}
