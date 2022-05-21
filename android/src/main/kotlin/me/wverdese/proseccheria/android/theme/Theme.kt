package me.wverdese.proseccheria.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Yellow200,
    primaryVariant = Yellow500,
    surface = Yellow200,
    secondary = Green200,
    secondaryVariant = Green200,
    onPrimary = Black,
    onSurface = Black,
)

private val LightColorPalette = lightColors(
    primary = Yellow500,
    primaryVariant = Yellow700,
    surface = Yellow500,
    secondary = Green500,
    secondaryVariant = Green700,
    onPrimary = Black,
    onSurface = Black,
)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
