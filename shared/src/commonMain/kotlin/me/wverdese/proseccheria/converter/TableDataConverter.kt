package me.wverdese.proseccheria.converter

import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.BOTTLE
import me.wverdese.proseccheria.model.Food
import me.wverdese.proseccheria.model.GLASS
import me.wverdese.proseccheria.model.ItemData
import me.wverdese.proseccheria.model.MenuItem
import me.wverdese.proseccheria.model.Other
import me.wverdese.proseccheria.model.QuantityType
import me.wverdese.proseccheria.model.TableId
import me.wverdese.proseccheria.model.VesselType
import me.wverdese.proseccheria.model.Wine

fun TableData.Item.asItemData(tableId: TableId) = ItemData(
    tableId = tableId,
    menuItemId = item.id,
    quantity = quantity,
    notes = notes,
    vessel = if (this is TableData.Item.WineItem) vessel else null,
)

fun ItemData?.asItem(menuItem: MenuItem): TableData.Item =
    when (menuItem) {
        is Food -> TableData.Item.FoodItem(
            item = menuItem,
            quantity = quantityOrDefault,
            notes = this?.notes
        )
        is Other -> TableData.Item.OtherItem(
            item = menuItem,
            quantity = quantityOrDefault,
            notes = this?.notes
        )
        is Wine -> TableData.Item.WineItem(
            item = menuItem,
            quantity = quantityOrDefault,
            notes = this?.notes,
            vessel = vesselOrDefault(menuItem.vessel)
        )
    }

private val ItemData?.quantityOrDefault: QuantityType get() = this?.quantity ?: 0

private fun ItemData?.vesselOrDefault(vessel: Wine.Vessel): VesselType =
    this?.vessel ?: if (vessel == Wine.Vessel.BOTTLE) BOTTLE else GLASS
