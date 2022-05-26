package me.wverdese.proseccheria.android.screen.order

import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.Table

data class OrderScreenState(
    val tables: List<Table>,
    val table: Table,
    val isClearTableButtonEnabled: Boolean,
    val groupedItems: Map<String, List<TableData.Item>>,
)
