package co.nimblehq.sample.compose.ui.theme

import androidx.compose.material.*
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Extend final [Colors] class to provide more custom app colors.
 */
data class AppColors(
    val themeColors: Colors,

    // Custom colors here
    val topAppBarBackground: Color = GreenCitrus
)

internal val LightColorPalette = AppColors(
    themeColors = lightColors()
)

internal val DarkColorPalette = AppColors(
    themeColors = darkColors()
)

internal val LocalColors = staticCompositionLocalOf { LightColorPalette }
