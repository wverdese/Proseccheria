package me.wverdese.proseccheria.model

import me.wverdese.proseccheria.asTwoDigits

typealias TableId = String

data class Table(val id: TableId, val name: String)

val tables = createTables(23)

fun createTables(size: Int) = List(size) {
    val index = it + 1
    Table(id = "T-${index.asTwoDigits()}", name = "Table $index")
}
