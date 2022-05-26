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
    val hasOrders: Boolean = items.find { it.hasOrder } != null

    sealed interface Item {
        val item: MenuItem
        val quantity: QuantityType
        val notes: NotesType?

        val hasOrder: Boolean get() = quantity > 0

        data class FoodItem(
            override val item: Food,
            override val quantity: QuantityType,
            override val notes: NotesType?,
        ): Item

        data class WineItem(
            override val item: Wine,
            override val quantity: QuantityType,
            override val notes: NotesType?,
            val vessel: VesselType,
        ): Item {
            val canChangeVessel: Boolean = item.vessel == Wine.Vessel.BOTH
        }

        data class OtherItem(
            override val item: Other,
            override val quantity: QuantityType,
            override val notes: NotesType?,
        ): Item
    }
}
