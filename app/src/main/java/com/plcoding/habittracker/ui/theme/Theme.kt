package com.plcoding.habittracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColors(
    val surfaceElevated: Color = Color.Unspecified,
    val surfaceBright: Color = Color.Unspecified,
    val success: Color = Color.Unspecified,
    val streak: Color = Color.Unspecified,
    val accent: Color = Color.Unspecified,
    val destructive: Color = Color.Unspecified,
    val textPrimary: Color = Color.Unspecified,
    val textSecondary: Color = Color.Unspecified,
    val textTertiary: Color = Color.Unspecified,
    val border: Color = Color.Unspecified
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = TextPrimary,
    secondary = Secondary,
    onSecondary = TextPrimary,
    tertiary = Accent,
    onTertiary = TextPrimary,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    error = Destructive,
    onError = TextPrimary,
    outline = Border
)

val customColors = CustomColors(
    surfaceElevated = SurfaceElevated,
    surfaceBright = SurfaceBright,
    success = Success,
    streak = Streak,
    accent = Accent,
    destructive = Destructive,
    textPrimary = TextPrimary,
    textSecondary = TextSecondary,
    textTertiary = TextTertiary,
    border = Border
)

object HabitTrackerTheme {
    val colors: CustomColors
        @Composable
        get() = LocalCustomColors.current

    @Composable
    operator fun invoke(
        content: @Composable () -> Unit
    ) {
        CompositionLocalProvider(LocalCustomColors provides customColors) {
            MaterialTheme(
                colorScheme = DarkColorScheme,
                typography = AppTypography,
                content = content
            )
        }
    }
}
