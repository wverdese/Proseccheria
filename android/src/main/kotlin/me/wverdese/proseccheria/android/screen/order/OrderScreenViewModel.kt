package me.wverdese.proseccheria.android.screen.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.TableId
import me.wverdese.proseccheria.repo.TableDataRepository

class OrderScreenViewModel(
    private val tableDataRepo: TableDataRepository
) : ViewModel() {
    val state: OrderScreenState by mutableStateOf(
        OrderScreenState(
            tables = tableDataRepo.tables,
            tableData = TableData(
                table = tableDataRepo.tables.first(),
                items = emptyList()
            )
        )
    )

    fun selectTable(tableId: TableId) {
        tableDataRepo.selectTable(tableId)
    }
}
