package com.blazon.app.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// Blazon Salon — Black & Gold premium theme
// Matches the brand menu / in-salon materials: pure black background,
// warm gold accent, dark gray elevated surfaces, subtle gold borders.
val BlazonBlack = Color(0xFF0A0A0A)            // Near-pure black background
val BlazonGold = Color(0xFFE5B93B)             // Warm brand gold
val BlazonGoldDeep = Color(0xFFB8891F)         // Deeper gold for gradients / pressed
val BlazonCard = Color(0xFF161616)             // Elevated surface (cards)
val BlazonSecondary = Color(0xFF1F1F1F)        // Secondary surface / chips
val BlazonMuted = Color(0xFF2A2A2A)            // Separators / outlines
val BlazonMutedForeground = Color(0xFF9A9A9A)  // Muted text
val BlazonForeground = Color(0xFFF5F5F5)       // Primary text
val BlazonDestructive = Color(0xFFEF4444)      // Error / destructive

val DarkColorScheme = darkColorScheme(
    primary = BlazonGold,
    onPrimary = BlazonBlack,
    secondary = BlazonSecondary,
    onSecondary = BlazonForeground,
    tertiary = BlazonMuted,
    onTertiary = BlazonForeground,
    background = BlazonBlack,
    onBackground = BlazonForeground,
    surface = BlazonCard,
    onSurface = BlazonForeground,
    surfaceVariant = BlazonSecondary,
    onSurfaceVariant = BlazonMutedForeground,
    error = BlazonDestructive,
    onError = BlazonForeground,
    outline = BlazonMuted,
    outlineVariant = BlazonSecondary
)
