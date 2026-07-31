package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Minimal Dark Color Scheme
fun getMinimalDarkColorScheme(colorTheme: String): ColorScheme {
    val (primary, primaryContainer, onPrimaryContainer) = when (colorTheme.uppercase()) {
        "EMERALD" -> Triple(Color(0xFF34D399), Color(0xFF064E3B), Color(0xFFA7F3D0))
        "OCEAN" -> Triple(Color(0xFF60A5FA), Color(0xFF1E3A8A), Color(0xFFBFDBFE))
        "TEAL" -> Triple(Color(0xFF2DD4BF), Color(0xFF134E4A), Color(0xFF99F6E4))
        "ROSE" -> Triple(Color(0xFFFB7185), Color(0xFF881337), Color(0xFFFECDD3))
        else -> Triple(Color(0xFF818CF8), Color(0xFF1E1B4B), Color(0xFFC7D2FE)) // INDIGO
    }

    return darkColorScheme(
        primary = primary,
        onPrimary = Color(0xFF0B0F17),
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = Color(0xFF34D399),
        onSecondary = Color(0xFF0B0F17),
        tertiary = Color(0xFFFBBF24),
        background = Color(0xFF090D16),
        surface = Color(0xFF131A26),
        surfaceVariant = Color(0xFF1C2536),
        onBackground = Color(0xFFF8FAFC),
        onSurface = Color(0xFFF1F5F9),
        onSurfaceVariant = Color(0xFF94A3B8),
        outline = Color(0xFF2D3748),
        outlineVariant = Color(0xFF1E293B)
    )
}

// Light Color Schemes per preset
fun getLightColorScheme(colorTheme: String): ColorScheme {
    val (primary, primaryContainer, onPrimaryContainer) = when (colorTheme.uppercase()) {
        "EMERALD" -> Triple(Color(0xFF059669), Color(0xFFECFDF5), Color(0xFF064E3B))
        "OCEAN" -> Triple(Color(0xFF2563EB), Color(0xFFEFF6FF), Color(0xFF1E3A8A))
        "TEAL" -> Triple(Color(0xFF0D9488), Color(0xFFF0FDFA), Color(0xFF134E4A))
        "ROSE" -> Triple(Color(0xFFE11D48), Color(0xFFFFF1F2), Color(0xFF881337))
        else -> Triple(BentoIndigoPrimary, BentoIndigoContainer, BentoIndigoDark) // INDIGO
    }

    return lightColorScheme(
        primary = primary,
        onPrimary = Color.White,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = BentoEmerald,
        onSecondary = Color.White,
        tertiary = BentoAmber,
        background = BentoSlateBg,
        surface = BentoSlateCard,
        surfaceVariant = Color(0xFFF1F5F9),
        onBackground = Color(0xFF0F172A),
        onSurface = Color(0xFF0F172A),
        onSurfaceVariant = Color(0xFF64748B),
        outline = BentoCardBorder,
        outlineVariant = BentoCardBorder
    )
}

@Composable
fun TakaTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorTheme: String = "INDIGO",
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> getMinimalDarkColorScheme(colorTheme)
        else -> getLightColorScheme(colorTheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

