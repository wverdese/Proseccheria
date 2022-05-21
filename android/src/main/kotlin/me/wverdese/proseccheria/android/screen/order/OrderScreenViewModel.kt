package me.wverdese.proseccheria.android.screen.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.TableId
import me.wverdese.proseccheria.repo.TableDataRepository

class OrderScreenViewModel(
    private val tableDataRepo: TableDataRepository
) : ViewModel() {
    var state: OrderScreenState by mutableStateOf(initScreenState())
        private set

    init {
        viewModelScope.launch {
            tableDataRepo.observeTableData.collect {
                state = state.copy(tableData = it)
            }
        }
    }

    fun selectTable(tableId: TableId) {
        tableDataRepo.selectTable(tableId)
    }

    private fun initScreenState() = OrderScreenState(
        tables = tableDataRepo.tables,
        tableData = TableData(
            table = tableDataRepo.tables.first(),
            items = emptyList()
        )
    )
}
