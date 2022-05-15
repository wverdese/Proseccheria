package me.wverdese.proseccheria.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

typealias ItemDataId = String
typealias QuantityType = Int
typealias NotesType = String
typealias VesselType = Int

const val GLASS: VesselType = 0
const val BOTTLE: VesselType = 1

@OptIn(ExperimentalSerializationApi::class)
private val format = Json { explicitNulls = false }

@Serializable
data class ItemData(
    val tableId: TableId,
    val menuItemId: MenuItemId,
    val quantity: QuantityType?,
    val notes: NotesType?,
    val vessel: VesselType? = null,
    val id: ItemDataId = id(tableId, menuItemId)
) {
    fun serialize() = format.encodeToString(this)

    companion object {
        fun parse(string: String): ItemData = format.decodeFromString(string)

        fun id(tableId: TableId, menuItemId: MenuItemId) = "$tableId:$menuItemId"
    }
}
