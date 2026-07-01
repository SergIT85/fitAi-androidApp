package com.by_korchagin.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * PREVIEW ДЛЯ ПРОВЕРКИ ТЕМЫ
 *
 * Тестируем:
 * - Фон с размытыми пятнами
 * - Цвета из темы
 * - Шрифты
 * - Размеры и отступы
 */

// === PREVIEW С ФОНОМ (основной) ===
@Preview(
    name = "Theme with Background",
    showSystemUi = true, // Показывает системные элементы (status bar, navigation)
    device = "id:pixel_5" // Можно убрать, если не нужно
)
@Suppress("LongMethod")
@Composable
fun ThemeWithBackgroundPreview() {
    FitnessAppTheme {
        FitnessBackground { // ← Применяем фон
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimensions.screenPadding),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMedium)
            ) {
                // === Проверка типографики ===
                Text(
                    text = "Hi!, Youssef",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Start a Workout",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Run 02 km",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spacingLarge))

                // === Проверка цветных блоков (как будущие кнопки/карточки) ===

                // Желтая кнопка
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimensions.buttonHeight)
                        .background(
                            color = MaterialTheme.colorScheme.secondary, // Желтый
                            shape = MaterialTheme.shapes.extraLarge // Pill форма
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Date",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryFixed,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                // Розовая карточка
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary, // Розовый
                            shape = MaterialTheme.shapes.medium // Скругленный
                        )
                )

                // Голубая карточка
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            color = MaterialTheme.colorScheme.tertiary, // Голубой
                            shape = MaterialTheme.shapes.medium
                        )
                )
            }
        }
    }
}

// === PREVIEW БЕЗ ФОНА (для сравнения) ===
@Preview(
    name = "Theme without Background",
    showBackground = true,
    backgroundColor = 0xFF262135 // Просто цвет без размытий
)
@Composable
private fun ThemeWithoutBackgroundPreview() {
    FitnessAppTheme {
        // Без FitnessBackground - просто контент
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Просто цвет
                .padding(MaterialTheme.dimensions.screenPadding),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMedium)
        ) {
            Text(
                text = "Hi!, Youssef",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Without fancy background",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimensions.buttonHeight)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
            )
        }
    }
}

// === КОМПАКТНЫЙ PREVIEW (для быстрой проверки цветов) ===
@Preview(
    name = "Color Palette",
    showBackground = true,
    widthDp = 320,
    heightDp = 400
)
@Composable
private fun ColorPalettePreview() {
    FitnessAppTheme {
        FitnessBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimensions.spacingMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingSmall)
            ) {
                Text(
                    text = "Color Palette",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                ColorSwatch("Primary", MaterialTheme.colorScheme.primary)
                ColorSwatch("Secondary", MaterialTheme.colorScheme.secondary)
                ColorSwatch("Tertiary", MaterialTheme.colorScheme.tertiary)
                ColorSwatch("Surface", MaterialTheme.colorScheme.surface)
                ColorSwatch("Error", MaterialTheme.colorScheme.error)
            }
        }
    }
}

// Вспомогательный компонент для показа цвета
@Composable
private fun ColorSwatch(name: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color, MaterialTheme.shapes.small)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
