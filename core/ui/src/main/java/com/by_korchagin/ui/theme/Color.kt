package com.by_korchagin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

// === BACKGROUND COLORS (Dark Theme) ===
private val DarkPurple = Color(0xFF262135)
private val DarkPurpleLight = Color(0xFF3A3544)
private val SurfaceDark = Color(0xFF4A4556)

// === ACCENT COLORS ===
private val PinkPrimary = Color(0xFFEDB4D8)
private val YellowPrimary = Color(0xFFF5E8B7)
private val BlueLight = Color(0xFFD4E8E8)

// === TEXT COLORS ===
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFB8B4C0)
private val TextHint = Color(0xFF8A8591)

private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF000000)

val FitnessColorScheme = darkColorScheme(
    primary = PinkPrimary,
    onPrimary = Black,

    secondary = YellowPrimary,
    onSecondary = Black,

    tertiary = BlueLight,
    onTertiary = Black,

    background = DarkPurple,
    onBackground = TextPrimary,

    surface = DarkPurpleLight,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceDark,
    onSurfaceVariant = TextSecondary,

    outline = TextSecondary.copy(alpha = 0.3f),
    outlineVariant = TextHint,

    error = Color(0xFFFF6B6B),
    onError = White,
)

/**
 * ADDITIONAL COLORS
 * For specific edge cases not covered by the Material3 ColorScheme
 */
object FitnessColors {
    val hint = TextHint
    val inactive = TextSecondary

    // === COLORS FOR BACKGROUND EFFECTS ===
    // Adding colors for blurred background glow spots
    val backgroundGlowPrimary = PinkPrimary       // Pink glow tint
    val backgroundGlowSecondary = PinkPrimary     // Can be adjusted to another color if needed

    // Alpha channels for glow effects (defined as constants)
    const val glowAlphaStrong = 0.22f    // For the top glow spot
    const val glowAlphaWeak = 0.03f      // For the bottom glow spot

    val success = Color(0xFF4CAF50)
    val warning = Color(0xFFFFC107)
}

/**
 * EXTENSION PROPERTIES for clean access to FitnessColors
 *
 * Enables using: MaterialTheme.fitnessColors.backgroundGlowPrimary
 * instead of: FitnessColors.backgroundGlowPrimary
 *
 * This keeps the API consistent:
 * - MaterialTheme.colorScheme.primary
 * - MaterialTheme.fitnessColors.backgroundGlowPrimary
 */
val MaterialTheme.fitnessColors: FitnessColors
    @Composable
    @ReadOnlyComposable
    get() = FitnessColors


