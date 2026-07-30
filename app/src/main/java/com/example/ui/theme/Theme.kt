package com.example.ui.theme

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
    primary = Color(0xFF818CF8),
    onPrimary = BentoIndigoDark,
    primaryContainer = BentoIndigoDark,
    onPrimaryContainer = Color(0xFFC7D2FE),
    secondary = BentoEmerald,
    onSecondary = Color.Black,
    tertiary = BentoAmber,
    background = SoftBackgroundDark,
    surface = CardSurfaceDark,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF8FAFC)
)

private val LightColorScheme = lightColorScheme(
    primary = BentoIndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = BentoIndigoContainer,
    onPrimaryContainer = BentoIndigoDark,
    secondary = BentoEmerald,
    onSecondary = Color.White,
    tertiary = BentoAmber,
    background = BentoSlateBg,
    surface = BentoSlateCard,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    outlineVariant = BentoCardBorder
)

@Composable
fun TakaTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent brand colors
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
