package com.by_korchagin.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview

/**
 * Renders the application background.
 *
 * The background consists of:
 * - A solid base color from MaterialTheme.colorScheme.background.
 * - Two blurred glow spots using colors from MaterialTheme.fitnessColors.
 *
 * All colors are dynamically resolved from the theme — zero hardcoding!
 *
 * @param modifier The modifier to be applied to the container.
 * @param content The composable content to be rendered on top of the background.
 */
@Composable
fun FitnessBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Resolve colors and properties from the theme
    val backgroundColor = MaterialTheme.colorScheme.background
    val glowColor = MaterialTheme.fitnessColors.backgroundGlowPrimary
    val glowAlphaStrong = FitnessColors.glowAlphaStrong
    val glowAlphaWeak = FitnessColors.glowAlphaWeak

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = modifier.fillMaxSize()) {
            // Draw the solid base theme background
            drawRect(color = backgroundColor)

            // Draw the top-left blurred glow spot
            drawBlurredCircle(
                center = Offset(
                    x = size.width * 0.21f,
                    y = size.height * 0.126f
                ),
                radius = size.height * 0.21f,
                color = glowColor.copy(alpha = glowAlphaStrong)
            )
            // Draw the bottom blurred glow spot
            drawBlurredCircle(
                center = Offset(
                    x = size.width * 0.43f,
                    y = size.height * 0.84f
                ),
                radius = size.width * 0.626f,
                color = glowColor.copy(alpha = glowAlphaWeak)
            )
        }
        content()
    }
}

/**
 * Draws a soft blurred circle using a radial gradient brush.
 * Simulates a SVG Gaussian Blur effect natively within the DrawScope.
 *
 * @param center The center coordinates of the circle.
 * @param radius The radius of the circle.
 * @param color The base tint color (pre-configured with target transparency).
 */

private fun DrawScope.drawBlurredCircle(
    center: Offset,
    radius: Float,
    color: Color
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color, // Center — peak brightness
                color.copy(alpha = color.alpha * 0.7f), // 70% of the initial alpha
                color.copy(alpha = color.alpha * 0.4f), // 40% of the initial alpha
                color.copy(alpha = color.alpha * 0.1f), // 10% of the initial alpha
                Color.Transparent // Edges — fully transparent
            ),
            center = center,
            radius = radius
        ),
        center = center,
        radius = radius
    )
}

// Preview wrapper to verify layout behavior
@Preview(showSystemUi = true)
@Composable
private fun BackgroundPreview() {
    FitnessAppTheme {
        FitnessBackground {
            // Empty body wrapper to isolate background inspection
        }
    }
}
