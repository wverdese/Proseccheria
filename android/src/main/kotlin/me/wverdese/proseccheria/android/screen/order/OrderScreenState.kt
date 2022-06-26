package me.wverdese.proseccheria.android.screen.order

import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.MenuItemId
import me.wverdese.proseccheria.model.Table

data class OrderScreenState(
    val searchText: String?,
    val tables: List<TableData.TableItem>,
    val table: Table,
    val isClearTableButtonEnabled: Boolean,
    val mode: Mode,
) {
    sealed interface Mode {
        data class Edit(val groupedItems: Map<String, List<TableData.Item>>) : Mode
        data class View(val orders: List<Order>) : Mode
    }
}

data class Order(val id: MenuItemId, val text: String, val notes: String?)
