package me.wverdese.proseccheria.model

typealias MenuItemId = String

sealed interface MenuItem {
    val id: MenuItemId
    val name: String
    val type: Type

    sealed class Type(val name: String) {
        val ordinal: Int by lazy { typeSorting.indexOf(this) }
    }
}

data class Food(
    override val id: MenuItemId,
    override val name: String,
    override val type: Type
) : MenuItem {
    sealed class Type(name: String): MenuItem.Type(name) {
        object Antipasto: Type("Antipasti")
        object Primo: Type("Pasta e risotti")
        object Secondo: Type("Secondi")
        object Dolce: Type("Dolci")
        object Lunch: Type("Lunch")
    }
}

data class Wine(
    override val id: MenuItemId,
    override val name: String,
    override val type: Type,
    val vessel: Vessel,
) : MenuItem {
    sealed class Type(name: String): MenuItem.Type(name) {
        object Prosecco: Type("Prosecchi e spumanti")
        object Spumante: Type("Spumanti rosè")
        object Rosso: Type("Vini rossi")
        object Bianco: Type("Vini bianchi")
        object Dessert: Type("Vini da dessert")
    }

    enum class Vessel {
        GLASS,
        BOTTLE,
        BOTH
    }
}

data class Other(
    override val id: MenuItemId,
    override val name: String,
    override val type: Type
) : MenuItem {
    sealed class Type(name: String): MenuItem.Type(name) {
        object Spirit: Type("Superalcolici e cocktails")
        object Beer: Type("Birre")
        object Soft: Type("Analcolici")
        object Cafeteria: Type("Caffetteria")
    }
}

private val typeSorting = listOf(
    Food.Type.Antipasto,
    Food.Type.Primo,
    Food.Type.Secondo,
    Food.Type.Lunch,
    Wine.Type.Prosecco,
    Wine.Type.Spumante,
    Wine.Type.Rosso,
    Wine.Type.Bianco,
    Other.Type.Beer,
    Other.Type.Soft,
    Wine.Type.Dessert,
    Food.Type.Dolce,
    Other.Type.Cafeteria,
    Other.Type.Spirit,
)
