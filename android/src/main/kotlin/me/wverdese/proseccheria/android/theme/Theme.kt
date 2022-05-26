package me.wverdese.proseccheria.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Yellow200,
    onPrimary = Black,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
)

private val LightColorPalette = lightColors(
    primary = Green500,
    onPrimary = Black,
    surface = Yellow500,
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
        typography = MaterialTheme.typography,
        shapes = Shapes,
        content = content
    )
}
