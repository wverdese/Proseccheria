package me.wverdese.proseccheria.android.screen.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
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
    private val searchText = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            combine(
                tableDataRepo.observeTableData,
                mode,
                searchText
            ) { data, mode, searchText ->
                val items: List<TableData.Item> = when {
                    searchText == null -> data.items
                    searchText.length > 1 -> data.items.filter {
                        it.item.name.contains(other = searchText, ignoreCase = true)
                    }
                    else -> emptyList()
                }
                state.copy(
                    searchText = searchText,
                    tables = data.tables,
                    table = data.table,
                    isClearTableButtonEnabled = data.hasOrders,
                    mode = if (mode == Mode.EDIT)
                        OrderScreenState.Mode.Edit(
                            groupedItems = items.groupBy { it.item.type.name }
                        )
                    else
                        OrderScreenState.Mode.View(
                            orders = items
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
                .flowOn(Dispatchers.IO)
                .collect {
                    state = it
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

    fun onNavigateToSearchMode() {
        searchText.value = ""
    }

    fun onNavigateBackFromSearchMode() {
        searchText.value = null
    }

    fun onSearchTextChanged(text: String) {
        searchText.value = text
    }

    fun onClearSearch() {
        searchText.value = ""
    }

    private fun initScreenState() = OrderScreenState(
        searchText = null,
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
