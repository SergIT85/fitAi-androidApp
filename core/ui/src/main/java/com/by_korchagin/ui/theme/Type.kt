package com.by_korchagin.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.by_korchagin.ui.R // Replace with your R class

/**
 * APPLICATION FONT
 *
 * FontFamily represents a font family with different weights and styles.
 * Compose automatically selects the correct weight when using fontWeight.
 */
private val InterFontFamily = FontFamily(
    Font(R.font.inter_tight_regular, FontWeight.Normal), // 400
    Font(R.font.inter_tight_semi_bold, FontWeight.SemiBold), // 600
    Font(R.font.inter_tight_bold, FontWeight.Bold) // 700
)

/**
 * APPLICATION TYPOGRAPHY
 *
 * Material3 Typography defines 15 text styles.
 * We configure only those used in the design.
 *
 * Naming structure:
 * - display: largest text styles (screen headlines)
 * - headline: headers
 * - title: subheadings
 * - body: primary body text
 * - label: text inside buttons and badges
 */
val FitnessTypography = Typography(
    // === LARGE HEADLINES ===

    displayLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp, // For large numbers (calendar date)
        lineHeight = 56.sp,
    ),

    // === HEADLINES ===

    headlineLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp, // "11,December 2022"
        lineHeight = 40.sp,
    ),

    headlineMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp, // "Your Schedule", "Hi!, Youssef"
        lineHeight = 32.sp,
    ),

    headlineSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp, // Bottom sheet / Dialog headers
        lineHeight = 28.sp,
    ),

    // === BODY TEXT ===

    titleLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp, // "Start a Workout"
        lineHeight = 28.sp,
    ),

    titleMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, // "WarmUp", "Pushups session"
        lineHeight = 24.sp,
    ),

    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp, // Primary text
        lineHeight = 24.sp,
    ),

    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp, // "Run 02 km", descriptions
        lineHeight = 20.sp,
    ),

    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp, // "Date", secondary minor text
        lineHeight = 16.sp,
    ),

    // === BUTTON AND LABEL TEXT ===

    labelLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, // Button text like "Start", "Continue"
        lineHeight = 24.sp,
    ),

    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp, // Smaller buttons
        lineHeight = 20.sp,
    ),

    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp, // Minor badges / labels
        lineHeight = 16.sp,
    ),
)
