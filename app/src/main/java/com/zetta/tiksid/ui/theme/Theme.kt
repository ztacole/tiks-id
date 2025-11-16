package com.zetta.tiksid.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = primaryColor,
    onPrimary = Color.White,
    secondary = secondaryColor,
    onSecondary = Color.White,
    tertiary = tertiaryColor,
    onTertiary = Color.White,
    background = surfaceColor,
    onBackground = Color.White,
    surface = surfaceColor,
    onSurface = Color.White,
    error = errorColor,
    onError = surfaceVariantColor,
    outline = primaryColor,
    primaryContainer = primaryColor,
    onPrimaryContainer = Color.White,
    secondaryContainer = secondaryColor,
    onSecondaryContainer = Color.White,
    tertiaryContainer = tertiaryColor,
    onTertiaryContainer = Color.White,
    surfaceVariant = surfaceVariantColor,
    onSurfaceVariant = Color.White,
    inverseSurface = Color.White,
    inverseOnSurface = Color.White,
    inversePrimary = Color.White,
    surfaceTint = Color.White,
    outlineVariant = primaryColor,
    scrim = Color.White,
    surfaceContainer = surfaceVariantColor,
    surfaceDim = Color.White,
    surfaceBright = Color.White,
    surfaceContainerHigh = surfaceVariantColor,
    surfaceContainerHighest = surfaceVariantColor,
    surfaceContainerLow = surfaceVariantColor,
    surfaceContainerLowest = surfaceVariantColor,
    errorContainer = errorColor,
    onErrorContainer = surfaceVariantColor,
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = MaterialTheme.shapes.copy(large = RoundedCornerShape(20.dp)),
        typography = AppTypography,
        content = content
    )
}