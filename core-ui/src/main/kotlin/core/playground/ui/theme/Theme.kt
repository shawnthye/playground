package core.playground.ui.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PlaygroundTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (useDarkColors) PlaygroundDarkColors else PlaygroundLightColors,
        content = content,
    )
}

@Composable
private fun PreviewContent() {
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

@Preview
@Composable
private fun PreviewInLightColors() {
    PlaygroundTheme { PreviewContent() }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewInDarkColors() {
    PlaygroundTheme { PreviewContent() }
}
