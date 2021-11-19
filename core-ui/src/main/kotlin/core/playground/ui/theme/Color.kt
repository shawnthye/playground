package core.playground.ui.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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

private val LIGHT = Color(0xFFFFFFFF)
private val DARK = Color(0xFF373740)

internal val PlaygroundLightColors = lightColors(
    primary = DARK,
    secondary = DARK,
    surface = Color.White,
)
internal val PlaygroundDarkColors = darkColors(
    primary = LIGHT,
    secondary = LIGHT,
    surface = DARK,
    background = DARK,
)

@Preview
@Composable
private fun PreviewLight() {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) PlaygroundDarkColors else PlaygroundLightColors,
    ) {
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
