package core.playground.ui.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Colors
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

private val Light = Color(0xFFFFFFFF)
private val Dark = Color(0xFF111827)
private val SystemIcon = Color(0xFF666666)

internal val PlaygroundLightColors = lightColors(
    primary = Dark,
    primaryVariant = Dark,
    secondary = Dark,
    secondaryVariant = Dark,
    background = Light,
    surface = Light,
    onBackground = SystemIcon,
    onSurface = SystemIcon,
    onPrimary = Light,
    onSecondary = Light,
)
internal val PlaygroundDarkColors = darkColors(
    primary = Light,
    primaryVariant = Light,
    secondary = Light,
    secondaryVariant = Light,
    background = Dark,
    surface = Dark,
    onBackground = Light,
    onSurface = Light,
    onPrimary = Dark,
    onSecondary = Dark,
)

val Colors.onSurfaceEmphasisMedium get() = onSurface.copy(alpha = 0.6f)
val Colors.onSurfaceEmphasisHighType get() = onSurface.copy(alpha = 0.87f)

@Preview
@Composable
private fun PreviewLight() {
    PlaygroundTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = { },
                modifier = Modifier.wrapContentSize(),
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(id = android.R.string.ok),
                )
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNight() {
    PreviewLight()
}
