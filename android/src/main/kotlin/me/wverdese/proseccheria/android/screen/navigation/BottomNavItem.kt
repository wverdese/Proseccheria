package me.wverdese.proseccheria.android.screen.navigation

import me.wverdese.proseccheria.android.R

sealed class BottomNavItem(val title: String, val icon: Int, val route: String) {
    object Order : BottomNavItem(title = "Order", icon = R.drawable.ic_restaurant, route = "order")
    object Hours : BottomNavItem(title = "Hours", icon = R.drawable.ic_clock, route = "hours")
}
