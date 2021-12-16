package core.playground.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

typealias Icons = Icons.Rounded

@Composable
fun PlaygroundTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (useDarkColors) PlaygroundDarkColors else PlaygroundLightColors,
        typography = PlaygroundTypography,
        shapes = PlaygroundShapes,
    ) {
        CompositionLocalProvider(
            LocalRippleTheme provides PlaygroundRippleTheme,
            content = content,
        )
    }
}

@Immutable
private object PlaygroundRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color {
        val isLight = MaterialTheme.colors.isLight
        val current = LocalContentColor.current
        val luminance = current.luminance()

        /**
         * Our night mode is using white color, so we have to specific handle here
         */
        if (luminance < 0.5f && !isLight) {
            return MaterialTheme.colors.surface
        }

        return RippleTheme.defaultRippleColor(
            contentColor = current,
            lightTheme = isLight,
        )
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        if (!MaterialTheme.colors.isLight) {
            return RippleAlpha(
                pressedAlpha = 0.24f,
                focusedAlpha = 0.20f,
                draggedAlpha = 0.16f,
                hoveredAlpha = 0.08f,
            )
        }

        return RippleTheme.defaultRippleAlpha(
            contentColor = LocalContentColor.current,
            lightTheme = MaterialTheme.colors.isLight,
        )
    }
}
