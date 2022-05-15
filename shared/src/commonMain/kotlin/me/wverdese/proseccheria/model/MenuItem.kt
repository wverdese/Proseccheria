package me.wverdese.proseccheria.model

typealias MenuItemId = String

sealed interface MenuItem {
    val id: MenuItemId
    val name: String
    val type: Type

    sealed interface Type {
        val name: String
    }
}

data class Food(
    override val id: MenuItemId,
    override val name: String,
    override val type: Type
) : MenuItem {

    sealed class Type(override val name: String): MenuItem.Type {
        object Antipasto: Type("Antipasti")
        object Primo: Type("Pasta e risotti")
        object Secondo: Type("Secondi")
        object Dolce: Type("Dolci")
    }
}

data class Wine(
    override val id: MenuItemId,
    override val name: String,
    override val type: Type,
    val vessel: Vessel,
) : MenuItem {
    sealed class Type(override val name: String): MenuItem.Type {
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
    sealed class Type(override val name: String): MenuItem.Type {
        object Spirit: Type("Superalcolici e cocktails")
        object Beer: Type("Birre")
        object Soft: Type("Analcolici")
        object Cafeteria: Type("Caffetteria")
    }
}
