package com.scrcasebook.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ScrAccentDark,
    onPrimary = ScrBackgroundDark,
    primaryContainer = Color(0xFF1D3A33),
    onPrimaryContainer = ScrAccentDark,
    secondary = ScrMutedDark,
    background = ScrBackgroundDark,
    surface = ScrSurfaceDark,
    onBackground = ScrTextDark,
    onSurface = ScrTextDark
)

private val LightColorScheme = lightColorScheme(
    primary = ScrPrimary,
    onPrimary = ScrOnPrimary,
    primaryContainer = ScrPrimaryContainer,
    onPrimaryContainer = ScrOnPrimaryContainer,
    secondary = ScrSecondary,
    background = ScrBackgroundLight,
    surface = ScrSurfaceLight,
    onBackground = ScrTextLight,
    onSurface = ScrTextLight
)

@Composable
fun SCRCaseBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep medical brand by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
