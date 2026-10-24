package com.example.servexa.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8075FF),
    secondary = androidx.compose.ui.graphics.Color(0xFFBCA6FF),
    tertiary = androidx.compose.ui.graphics.Color(0xFFFFB52E),

    background = androidx.compose.ui.graphics.Color(0xFF121218),
    surface = androidx.compose.ui.graphics.Color(0xFF1C1B22),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF282732),

    onBackground = androidx.compose.ui.graphics.Color(0xFFF4F2F8),
    onSurface = androidx.compose.ui.graphics.Color(0xFFF4F2F8),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFBDB8C9),

    outline = androidx.compose.ui.graphics.Color(0xFF777181),
    outlineVariant = Color(0xFF353342)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750F5),
    secondary = Color(0xFF5141D9),
    tertiary = Color(0xFFFF941F),

    background = Color(0xFFF8F7FC),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFF1EFFF),

    onBackground = androidx.compose.ui.graphics.Color(0xFF18181B),
    onSurface = androidx.compose.ui.graphics.Color(0xFF18181B),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF71717A),

    outline = androidx.compose.ui.graphics.Color(0xFFA1A1AA),
    outlineVariant = androidx.compose.ui.graphics.Color(0xFFE4E4E7)
)

@Composable
fun ServexaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}