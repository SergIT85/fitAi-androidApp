package com.by_korchagin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * MAIN APPLICATION THEME
 *
 * This Composable wraps the entire application and provides:
 * - Colors (colorScheme)
 * - Typography (typography)
 * - Shapes (shapes)
 * - Dimensions (dimensions via CompositionLocal)
 *
 * USAGE:
 * Wrap your content inside MainActivity or within any Preview block:
 *
 * FitnessAppTheme {
 *     MyScreen()
 * }
 */
@Composable
fun FitnessAppTheme(
    content: @Composable () -> Unit
) {
    // CompositionLocalProvider implicitly provides our custom Dimensions to all child composables
    CompositionLocalProvider(
        LocalDimensions provides Dimensions()
    ) {
        MaterialTheme(
            colorScheme = FitnessColorScheme, // Our custom color scheme
            typography = FitnessTypography, // Our custom typography
            shapes = FitnessShapes, // Our custom shapes
            content = content // Application content hierarchy
        )
    }
}

/**
 * EXTENSION PROPERTY for clean access to Dimensions
 *
 * Enables using: MaterialTheme.dimensions.spacingMedium
 * instead of: LocalDimensions.current.spacingMedium
 */
val MaterialTheme.dimensions: Dimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current
