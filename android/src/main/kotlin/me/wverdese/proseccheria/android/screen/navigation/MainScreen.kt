package me.wverdese.proseccheria.android.screen.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import me.wverdese.proseccheria.android.theme.AppTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Column {
        Box(modifier = Modifier.weight(1f)) {
            NavigationGraph(navController = navController)
        }
        BottomNavigationWidget(navController = navController)
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen()
    }
}
