package me.wverdese.proseccheria.android.screen.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.domain.firstTableItems
import me.wverdese.proseccheria.model.GLASS
import me.wverdese.proseccheria.model.NotesType
import me.wverdese.proseccheria.model.Table
import me.wverdese.proseccheria.model.VesselType
import me.wverdese.proseccheria.model.firstTable
import me.wverdese.proseccheria.repo.TableDataRepository

class OrderScreenViewModel(
    private val tableDataRepo: TableDataRepository
) : ViewModel() {
    var state: OrderScreenState by mutableStateOf(initScreenState())
        private set

    private val mode = MutableStateFlow(Mode.EDIT)

    init {
        viewModelScope.launch {
            tableDataRepo
                .observeTableData
                .combine(mode) { data, mode -> data to mode }
                .collect { (data, mode) ->
                    state = state.copy(
                        tables = data.tables,
                        table = data.table,
                        isClearTableButtonEnabled = data.hasOrders,
                        mode = if (mode == Mode.EDIT)
                            OrderScreenState.Mode.Edit(
                                groupedItems = data.items.groupBy { it.item.type.name }
                            )
                        else
                            OrderScreenState.Mode.View(
                                orders = data.items
                                    .filter { it.hasOrder }
                                    .map { item ->
                                        var text = "${item.quantity}x ${item.item.name}"
                                        if (item is TableData.Item.WineItem) {
                                            text += " (${item.vessel.asVesselString()})"
                                        }
                                        Order(id = item.item.id, text, item.notes)
                                    }
                            )
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

    fun toggleMode() {
        mode.value = if (mode.value == Mode.EDIT) Mode.VIEW else Mode.EDIT
    }

    private fun initScreenState() = OrderScreenState(
        tables = firstTableItems(),
        table = firstTable(),
        isClearTableButtonEnabled = false,
        mode = OrderScreenState.Mode.Edit(groupedItems = emptyMap())
    )
}

private fun VesselType.asVesselString() = if (this == GLASS) "glass" else "bottle"

private enum class Mode {
    EDIT, VIEW
}
