package me.wverdese.proseccheria.android.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.wverdese.proseccheria.android.screen.hours.HoursScreen
import me.wverdese.proseccheria.android.screen.order.OrderScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.Order.route) {
        composable(BottomNavItem.Order.route) {
            OrderScreen()
        }
        composable(BottomNavItem.Hours.route) {
            HoursScreen()
        }
    }
}
