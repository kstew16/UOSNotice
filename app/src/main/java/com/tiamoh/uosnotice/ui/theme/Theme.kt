package com.tiamoh.uosnotice.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = UOSMain,
    background = Color.Black,
    surface = Color.Gray,
    onPrimary = Color.Green,
    onSecondary = Color.Blue,
    onBackground = UOSMain,
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
    val view = LocalView.current
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(Color.Red)
    if(darkTheme){
        systemUiController.setSystemBarsColor(
            color = UOSMain
        )
    }else{
        systemUiController.setSystemBarsColor(
            color = UOSMain
        )
    }


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