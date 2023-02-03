package com.tiamoh.uosnotice.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = UOSMain,
    background = Color.Red,
    surface = Color.Gray,
    onPrimary = Color.Green,
    onSecondary = Color.Blue,
    onBackground = Purple200,
    primaryVariant = UOSLight,
    secondary = UOSBright
)

private val LightColorPalette = lightColors(
    primary = UOSMain,
    primaryVariant = UOSLight,
    secondary = UOSBright

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun UosNoticeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
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