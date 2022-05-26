package me.wverdese.proseccheria.android.screen.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.NotesType
import me.wverdese.proseccheria.model.QuantityType
import me.wverdese.proseccheria.model.TableId
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
                    groupedItems = data.items
                        .groupBy { it.item.type.name }
                        .mapValues { (_, list) -> list.sortedBy { it.item.name } }
                )
            }
        }
    }

    fun selectTable(tableId: TableId) {
        tableDataRepo.selectTable(tableId)
    }

    fun editNotes(item: TableData.Item, notes: NotesType?) {
        viewModelScope.launch {
            tableDataRepo.updateNotes(state.table.id, item, notes)
        }
    }

    fun incrementQuantity(item: TableData.Item, quantity: QuantityType) {
        viewModelScope.launch {
            tableDataRepo.updateQuantity(state.table.id, item, quantity + 1)
        }
    }

    fun decrementQuantity(item: TableData.Item, quantity: QuantityType) {
        viewModelScope.launch {
            tableDataRepo.updateQuantity(state.table.id, item, quantity - 1)
        }
    }

    private fun initScreenState() = OrderScreenState(
        tables = tableDataRepo.tables,
        table = tableDataRepo.tables.first(),
        groupedItems = emptyMap(),
    )
}
