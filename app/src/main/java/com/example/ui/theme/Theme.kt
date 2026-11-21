package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val DengueGuardDarkColorScheme = darkColorScheme(
  primary = CyanBright,
  onPrimary = NavyVoid,
  primaryContainer = CyanMuted,
  onPrimaryContainer = CyanBright,
  secondary = TealAccent,
  onSecondary = NavyVoid,
  secondaryContainer = TealMuted,
  onSecondaryContainer = TealBright,
  tertiary = RiskHighOrange,
  onTertiary = NavyVoid,
  background = NavyVoid,
  onBackground = TextPrimary,
  surface = NavyDeep,
  onSurface = TextPrimary,
  surfaceVariant = CharcoalNavy,
  onSurfaceVariant = TextSecondary,
  surfaceContainerLowest = NavyVoid,
  surfaceContainerLow = CharcoalNavy,
  surfaceContainer = CharcoalCard,
  surfaceContainerHigh = CharcoalElevated,
  surfaceContainerHighest = SlateBorder,
  outline = SlateBorder,
  outlineVariant = SlateBorderStrong,
  error = RiskCriticalRed,
  onError = TextPrimary,
  errorContainer = RiskCriticalBg,
  onErrorContainer = RiskCriticalRed,
)

val DengueGuardShapes = Shapes(
  extraSmall = RoundedCornerShape(6.dp),
  small = RoundedCornerShape(10.dp),
  medium = RoundedCornerShape(14.dp),
  large = RoundedCornerShape(18.dp),
  extraLarge = RoundedCornerShape(24.dp),
)

@Composable
fun DengueGuardTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = DengueGuardDarkColorScheme,
    typography = AppTypography,
    shapes = DengueGuardShapes,
    content = content,
  )
}
