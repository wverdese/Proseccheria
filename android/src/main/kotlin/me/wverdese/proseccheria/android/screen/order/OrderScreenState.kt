package me.wverdese.proseccheria.android.screen.order

import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.Table

data class OrderScreenState(
    val tables: List<Table>,
    val tableData: TableData,
)
