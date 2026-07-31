package com.example.shared.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Minimal Dark Color Scheme
fun getMinimalDarkColorScheme(colorTheme: String): ColorScheme {
    val (primary, primaryContainer, onPrimaryContainer) = when (colorTheme.uppercase()) {
        "EMERALD" -> Triple(Color(0xFF00C48C), Color(0xFF003D2C), Color(0xFFA7F3D0))
        "OCEAN"   -> Triple(Color(0xFF60A5FA), Color(0xFF1E3A8A), Color(0xFFBFDBFE))
        "TEAL"    -> Triple(Color(0xFF2DD4BF), Color(0xFF134E4A), Color(0xFF99F6E4))
        "ROSE"    -> Triple(Color(0xFFFF4757), Color(0xFF5C0011), Color(0xFFFFCDD2))
        else      -> Triple(Color(0xFF8B7CF8), Color(0xFF2D1F6E), Color(0xFFD6CFFE)) // INDIGO
    }

    return darkColorScheme(
        primary = primary,
        onPrimary = Color(0xFF080810),
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = Color(0xFF00C48C),
        onSecondary = Color(0xFF080810),
        tertiary = Color(0xFFFFA827),
        background = Color(0xFF080810),
        surface = Color(0xFF111118),
        surfaceVariant = Color(0xFF1A1A27),
        onBackground = Color(0xFFF5F4F2),
        onSurface = Color(0xFFF5F4F2),
        onSurfaceVariant = Color(0xFF8B8B99),
        outline = Color(0xFF2A2A3A),
        outlineVariant = Color(0xFF1E1E2E)
    )
}

// Light Color Schemes per preset — premium warm palette
fun getLightColorScheme(colorTheme: String): ColorScheme {
    val (primary, primaryContainer, onPrimaryContainer) = when (colorTheme.uppercase()) {
        "EMERALD" -> Triple(Color(0xFF00A876), Color(0xFFE6FBF4), Color(0xFF003D2C))
        "OCEAN"   -> Triple(Color(0xFF2563EB), Color(0xFFEFF6FF), Color(0xFF1E3A8A))
        "TEAL"    -> Triple(Color(0xFF0D9488), Color(0xFFF0FDFA), Color(0xFF134E4A))
        "ROSE"    -> Triple(Color(0xFFFF4757), Color(0xFFFFEBED), Color(0xFF5C0011))
        else      -> Triple(PremiumViolet, PremiumVioletSoft, Color(0xFF3D2DB5)) // INDIGO
    }

    return lightColorScheme(
        primary = primary,
        onPrimary = Color.White,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = PremiumEmerald,
        onSecondary = Color.White,
        tertiary = PremiumAmber,
        background = PremiumBg,
        surface = PremiumSurface,
        surfaceVariant = Color(0xFFF0EFF0),
        onBackground = PremiumBlack,
        onSurface = PremiumBlack,
        onSurfaceVariant = PremiumSlate,
        outline = PremiumBorder,
        outlineVariant = PremiumBorder
    )
}

@Composable
fun TakaTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorTheme: String = "INDIGO",
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        getMinimalDarkColorScheme(colorTheme)
    } else {
        getLightColorScheme(colorTheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
