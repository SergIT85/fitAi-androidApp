package com.by_korchagin.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.by_korchagin.ui.theme.FitnessAppTheme
import com.by_korchagin.ui.theme.FitnessBackground
import com.by_korchagin.ui.theme.dimensions

/**
 * PRIMARY BUTTON
 *
 * Main action button with yellow background and pill shape.
 * Used for primary actions like "Start", "Continue", etc.
 *
 * Design features:
 * - Yellow background (MaterialTheme.colorScheme.secondary)
 * - Black text (MaterialTheme.colorScheme.onSecondary)
 * - Pill shape (fully rounded corners)
 * - Optional icon on the right
 * - 56dp height (standard touch target)
 *
 * @param text Button text
 * @param onClick Click handler
 * @param modifier Modifier for customization
 * @param enabled Whether button is enabled (default: true)
 * @param icon Optional trailing icon (composable lambda)
 *
 * Usage:
 * ```
 * PrimaryButton(
 *     text = "Start",
 *     onClick = { /* action */ },
 *     icon = { Icon(Icons.Default.PlayArrow, null) }
 * )
 * ```
 */

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showDefaultIcon: Boolean = true,
    icon: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
        enabled = enabled,
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.38f),
            disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.38f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(
            horizontal = 32.dp,
            vertical = 16.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )

            // Show icon: custom takes precedence over default
            when {
                // If custom icon provided, use it
                icon != null -> {
                    Spacer(modifier = Modifier.width(8.dp))
                    icon()
                }
                // If showDefaultIcon is true, show arrow
                showDefaultIcon -> {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "▶",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                // Otherwise, no icon
            }
        }
    }
}

// ==================== PREVIEWS ====================
// ==================== PREVIEWS ====================

@Preview(name = "Primary Button - Default")
@Composable
private fun PrimaryButtonPreview() {
    FitnessAppTheme {
        FitnessBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimensions.screenPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    text = "Start",
                    onClick = { }
                )
            }
        }
    }
}

@Preview(name = "Primary Button - States")
@Composable
private fun PrimaryButtonStatesPreview() {
    FitnessAppTheme {
        FitnessBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimensions.screenPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Primary Button States",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Default with arrow
                PrimaryButton(
                    text = "Start",
                    onClick = { }
                )

                // Without icon
                PrimaryButton(
                    text = "Continue",
                    onClick = { },
                    showDefaultIcon = false
                )

                // Disabled
                PrimaryButton(
                    text = "Disabled",
                    onClick = { },
                    enabled = false
                )

                // Custom width
                PrimaryButton(
                    text = "Write",
                    onClick = { },
                    modifier = Modifier.width(200.dp)
                )
            }
        }
    }
}

@Preview(name = "Primary Button - All variants", showBackground = true)
@Composable
private fun PrimaryButtonVariantsPreview() {
    FitnessAppTheme {
        FitnessBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "From your design:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                PrimaryButton(text = "Start", onClick = { })
                PrimaryButton(text = "Write", onClick = { })
                PrimaryButton(text = "Look", onClick = { })
                PrimaryButton(
                    text = "Continue",
                    onClick = { },
                    showDefaultIcon = false
                )
            }
        }
    }
}


private const val ALPHA_DEFAULT_DISABLED = 0.38f