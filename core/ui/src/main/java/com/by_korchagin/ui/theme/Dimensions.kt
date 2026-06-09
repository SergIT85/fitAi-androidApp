package com.by_korchagin.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * SPACING AND DIMENSION SYSTEM
 *
 * This is NOT a standard part of Material3, so we create our own custom structure.
 * We use a data class to group related design tokens and dimensions together.
 */
data class Dimensions(
    // === SPACING (Margins between elements) ===
    val spacingXXSmall: Dp = 2.dp,
    val spacingXSmall: Dp = 4.dp,
    val spacingSmall: Dp = 8.dp,
    val spacingMedium: Dp = 16.dp,
    val spacingLarge: Dp = 24.dp,
    val spacingXLarge: Dp = 32.dp,
    val spacingXXLarge: Dp = 48.dp,

    // === PADDING (Internal component paddings) ===
    val paddingSmall: Dp = 8.dp,
    val paddingMedium: Dp = 12.dp,
    val paddingLarge: Dp = 16.dp,
    val paddingXLarge: Dp = 20.dp,

    // === COMPONENT-SPECIFIC DIMENSIONS ===
    val cardPadding: Dp = 20.dp, // Internal padding inside cards
    val screenPadding: Dp = 16.dp, // Padding from the screen edges
    val bottomBarHeight: Dp = 72.dp, // Height of the Bottom Navigation bar
    val iconSizeSmall: Dp = 20.dp, // Small icons
    val iconSizeMedium: Dp = 24.dp, // Medium/Standard icons
    val iconSizeLarge: Dp = 32.dp, // Large icons
    val avatarSize: Dp = 48.dp, // User avatar size
    val buttonHeight: Dp = 56.dp, // Standard button height
)

/**
 * CompositionLocal for providing and accessing dimensions across the app
 *
 * This is an advanced Compose pattern — it establishes a "context" scope
 * that will be implicitly available throughout the entire component tree.
 */
val LocalDimensions = staticCompositionLocalOf { Dimensions() }
