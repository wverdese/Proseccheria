package me.wverdese.proseccheria.domain

import me.wverdese.proseccheria.model.Food
import me.wverdese.proseccheria.model.MenuItem
import me.wverdese.proseccheria.model.NotesType
import me.wverdese.proseccheria.model.Other
import me.wverdese.proseccheria.model.QuantityType
import me.wverdese.proseccheria.model.Table
import me.wverdese.proseccheria.model.VesselType
import me.wverdese.proseccheria.model.Wine

data class TableData(
    val table: Table,
    val items: List<Item>
) {
    sealed interface Item {
        val hasStoredData: Boolean
        val item: MenuItem
        val quantity: QuantityType
        val notes: NotesType?

        data class FoodItem(
            override val hasStoredData: Boolean,
            override val item: Food,
            override val quantity: QuantityType,
            override val notes: NotesType?,
        ): Item

        data class WineItem(
            override val hasStoredData: Boolean,
            override val item: Wine,
            override val quantity: QuantityType,
            override val notes: NotesType?,
            val vessel: VesselType,
        ): Item

        data class OtherItem(
            override val hasStoredData: Boolean,
            override val item: Other,
            override val quantity: QuantityType,
            override val notes: NotesType?,
        ): Item
    }
}
