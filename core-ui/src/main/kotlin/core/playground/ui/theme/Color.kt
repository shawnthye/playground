package core.playground.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val DeviantArtColor = Color(0xFF00e59b)

private val LIGHT = Color(0xFFFFFFFF)
private val DARK = Color(0xFF000000)

val PlaygroundLightColors = lightColors(
    primary = DARK,
    secondary = DARK,
)

val PlaygroundDarkColors = darkColors(
    primary = LIGHT,
    secondary = LIGHT,
)
