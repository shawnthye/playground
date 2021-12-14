package app.playground.ui.debug.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import core.playground.ui.theme.PlaygroundTheme

private val DebugLight = Color(0xFFFEDBD0)
private val DebugDark = Color(0xFF442C2E)

private val DebugLightColors = lightColors(
    primary = DebugDark,
    primaryVariant = DebugDark,
    secondary = DebugDark,
    secondaryVariant = DebugDark,
    background = DebugLight,
    surface = DebugLight,
    onBackground = DebugDark,
    onSurface = DebugDark,
    onPrimary = DebugLight,
    onSecondary = DebugLight,
)

private val DebugDarkColors = darkColors(
    primary = DebugLight,
    primaryVariant = DebugLight,
    secondary = DebugLight,
    secondaryVariant = DebugLight,
    background = DebugDark,
    surface = DebugDark,
    onBackground = DebugLight,
    onSurface = DebugLight,
    onPrimary = DebugDark,
    onSecondary = DebugDark,
)

@Composable
internal fun DebugTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // copy the main shape from default theme
    PlaygroundTheme(useDarkColors = useDarkColors) {
        MaterialTheme(
            colors = if (isSystemInDarkTheme()) DebugDarkColors else DebugLightColors,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content,
        )
    }
}
