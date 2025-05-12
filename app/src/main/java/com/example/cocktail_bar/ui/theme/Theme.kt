package com.example.cocktail_bar.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkGreen,
    secondary = Lime,
    tertiary = Lime,
    background = DeepBlue,
)

private val LightColorScheme = lightColorScheme(
    primary = Pink,
    secondary = Yellow,
    tertiary = Yellow,
    background = Pink80,
    onPrimary = DarkPink,
)

@Composable
fun CocktailBarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val context = LocalContext.current
    val view = LocalView.current
    val window = (context as? Activity)?.window
    window?.let {
        // Set light status bar icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(it, view).isAppearanceLightStatusBars = true
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}