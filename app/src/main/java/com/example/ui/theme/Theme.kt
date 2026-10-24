package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = SimGreenPrimary,
  onPrimary = Color.White,
  primaryContainer = SimGreenContainer,
  onPrimaryContainer = SimGreenDark,
  secondary = SimRedPrimary,
  onSecondary = Color.White,
  secondaryContainer = SimRedContainer,
  onSecondaryContainer = SimRedDark,
  tertiary = SimStatBlue,
  background = SimBgLight,
  onBackground = SimTextPrimary,
  surface = SimWhite,
  onSurface = SimTextPrimary,
  surfaceVariant = Color(0xFFF1F5F9),
  onSurfaceVariant = SimTextSecondary,
  outline = SimBorder,
  outlineVariant = SimDivider,
  error = SimRedPrimary,
  onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFF4ADE80),
  onPrimary = Color(0xFF052E16),
  primaryContainer = SimGreenDark,
  onPrimaryContainer = Color(0xFFDCFCE7),
  secondary = Color(0xFFF87171),
  onSecondary = Color(0xFF450A0A),
  secondaryContainer = SimRedDark,
  onSecondaryContainer = Color(0xFFFEE2E2),
  background = Color(0xFF0F172A),
  onBackground = Color(0xFFF8FAFC),
  surface = Color(0xFF1E293B),
  onSurface = Color(0xFFF8FAFC),
  outline = Color(0xFF334155),
  outlineVariant = Color(0xFF1E293B)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our intentional SIMS Red/Green theme consistently
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
