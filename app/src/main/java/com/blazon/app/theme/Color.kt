package com.blazon.app.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// Premium Dark Theme - Black & Gold
val BlazonBlack = Color(0xFF0A0A0A)
val BlazonGold = Color(0xFFD4AF37)
val BlazonCard = Color(0xFF1A1A1A)
val BlazonSecondary = Color(0xFF2A2A2A)
val BlazonMuted = Color(0xFF3A3A3A)
val BlazonMutedForeground = Color(0xFFA0A0A0)
val BlazonForeground = Color(0xFFF5F5F5)
val BlazonDestructive = Color(0xFFEF4444)

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
    outline = BlazonSecondary,
    outlineVariant = BlazonMuted
)

