package me.wverdese.proseccheria.android

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import me.wverdese.proseccheria.android.theme.AppTheme
import me.wverdese.proseccheria.android.screen.order.OrderScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent {
            AppTheme {
                OrderScreen()
            }
        }
    }
}
