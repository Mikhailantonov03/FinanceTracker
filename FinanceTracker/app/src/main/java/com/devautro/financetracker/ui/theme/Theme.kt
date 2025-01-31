package com.devautro.financetracker.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = secondary,
    onPrimary = OnBackgroundColor,
    secondary = AccentBlue,
    onSecondary = OnAccentBlue,
    tertiary = CancelButton,
    onTertiary = UnChosenTextColor,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    primaryContainer = SurfaceColor,
    onPrimaryContainer = BackgroundColor,
    errorContainer = DarkRedCircle,
    onErrorContainer = OnBackgroundColor,
    error = LightRed,
    surface = Color.Transparent
)


private val LightColorScheme = lightColorScheme(
    primary = LightSecondary,
    onPrimary = LightOnBackground,
    secondary = LightAccentBlue,
    onSecondary = LightOnAccentBlue,
    tertiary = LightCancelButton,
    onTertiary = LightUnChosenTextColor,
    background = LightBackground,
    onBackground = LightOnBackground,
    primaryContainer = LightSurfaceColor,
    onPrimaryContainer = Color.Black,
    errorContainer = DarkRedCircle,
    onErrorContainer = OnBackgroundColor,
    error = DarkRedCircle,
    surface = Color.Transparent


)

@Composable
fun FinanceTrackerTheme(
    darkTheme: Boolean,

    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.background.toArgb()

        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}