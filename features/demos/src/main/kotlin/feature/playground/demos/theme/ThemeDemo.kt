package feature.playground.demos.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.theme.PlaygroundTheme

private val NOOP: () -> Unit = { /* NOOP */ }

@Composable
internal fun ThemeDemo() {

    Scaffold(modifier = Modifier.statusBarsPadding()) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Component("Button") {
                    Button(onClick = NOOP) { ButtonText() }
                    TextButton(onClick = NOOP) { ButtonText() }

                    OutlinedButton(onClick = NOOP) { ButtonText() }
                }

                Component("FAB") {
                    ExtendedFloatingActionButton(
                        onClick = NOOP,
                        text = { ButtonText() },
                        icon = { IconAdd() },
                    )

                    FloatingActionButton(onClick = NOOP) { IconAdd() }

                    FloatingActionButton(onClick = NOOP, Modifier.size(40.dp)) { IconAdd() }
                }

                Component("Shapes") {
                    Surface(
                        modifier = Modifier
                            .size(96.dp)
                            .clickable { },
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Small",
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .size(96.dp)
                            .clickable { },
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "Medium")
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .size(96.dp)
                            .clickable { },
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "Large")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Component(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {

    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.overline,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
private fun ButtonText() {
    Text(text = "Button")
}

@Composable
private fun IconAdd() {
    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
}

@Preview
@Composable
private fun PreviewLight() {
    PlaygroundTheme {
        ThemeDemo()
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNight() {
    PreviewLight()
}
