package me.wverdese.proseccheria.converter

import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.Food
import me.wverdese.proseccheria.model.ItemData
import me.wverdese.proseccheria.model.MenuItem
import me.wverdese.proseccheria.model.Other
import me.wverdese.proseccheria.model.TableId
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
        is Food -> TableData.Item.FoodItem(item = menuItem, quantity = this?.quantity, notes = this?.notes)
        is Other -> TableData.Item.OtherItem(item = menuItem, quantity = this?.quantity, notes = this?.notes)
        is Wine -> TableData.Item.WineItem(item = menuItem, quantity = this?.quantity, notes = this?.notes, vessel = this?.vessel)
    }
