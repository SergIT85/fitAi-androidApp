@file:Suppress("MagicNumber")

package com.by_korchagin.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * APPLICATION SHAPES (CORNER RADII)
 *
 * Material3 uses 5 baseline corner sizes:
 * - extraSmall: for minor elements
 * - small: for chips, small buttons
 * - medium: for cards
 * - large: for inputs, large cards
 * - extraLarge: for modal dialogs, pill buttons
 */
val FitnessShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp), // Minor elements
    small = RoundedCornerShape(12.dp), // Checkboxes, small buttons
    medium = RoundedCornerShape(20.dp), // Workout cards
    large = RoundedCornerShape(28.dp), // Inputs, Bottom Navigation
    extraLarge = RoundedCornerShape(50), // Pill buttons (50% = fully rounded)
)

/**
 * ADDITIONAL SHAPES
 * For specific corner radii that do not fit into the standard system
 */
object CustomShapes {
    val pill = RoundedCornerShape(percent = 50) // Fully rounded shape
    val topRounded = RoundedCornerShape(
        topStart = 28.dp,
        topEnd = 28.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    ) // Rounded top corners only (useful for Bottom Sheets)
}
