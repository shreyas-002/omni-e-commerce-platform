package com.example.omnie_commerceplatform.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val OmniColorScheme = lightColorScheme(
    primary = OmniDarkNavy,
    onPrimary = OmniWhite,
    secondary = OmniAccentPink,
    onSecondary = OmniWhite,
    background = OmniCreamBg,
    onBackground = OmniTextPrimary,
    surface = OmniWhite,
    onSurface = OmniTextPrimary,
    surfaceVariant = OmniCreamBg,
    onSurfaceVariant = OmniTextSecondary
)

@Composable
fun OmniTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = OmniColorScheme,
        content = content
    )
}
