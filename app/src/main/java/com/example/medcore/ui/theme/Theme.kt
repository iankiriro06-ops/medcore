package com.medcore.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.medcore.ui.theme.MedCoreTypography

private val MedCoreDarkColorScheme = darkColorScheme(
    primary            = CyanCore,
    onPrimary          = TextOnAccent,
    primaryContainer   = CyanSubtle,
    onPrimaryContainer = CyanCore,

    secondary          = IndigoCore,
    onSecondary        = TextOnAccent,
    secondaryContainer = IndigoSubtle,
    onSecondaryContainer = IndigoCore,

    tertiary           = TealCore,
    onTertiary         = TextOnAccent,
    tertiaryContainer  = TealSubtle,
    onTertiaryContainer = TealCore,

    background         = BackgroundDeep,
    onBackground       = TextPrimary,

    surface            = BackgroundSurface,
    onSurface          = TextPrimary,
    surfaceVariant     = BackgroundCard,
    onSurfaceVariant   = TextSecondary,

    outline            = BorderSubtle,
    outlineVariant     = BorderCard,

    error              = RoseCore,
    onError            = TextOnAccent,
    errorContainer     = RoseSubtle,
    onErrorContainer   = RoseCore,
)

@Composable
fun MedCoreTheme(
    darkTheme: Boolean = true, // Always dark by default
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MedCoreDarkColorScheme,
        typography  = MedCoreTypography,
        content     = content
    )
}