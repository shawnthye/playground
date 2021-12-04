package feature.playground.demos.error

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.DrawerAppBar
import core.playground.ui.theme.PlaygroundTheme

private val NOOP: () -> Unit = { /* NOOP */ }

@Composable
fun ErrorDemo(navigateUp: NavigateUp) {

    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = core.playground.ui.R.string.menu_error_demo,
                navigationUp = navigateUp,
            )
        },
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                Component("FAB") {
                    ExtendedFloatingActionButton(
                        onClick = NOOP,
                        text = { ButtonText() },
                        icon = { IconAdd() },
                    )
                }
            }
        }
    }
}

@Composable
private fun Component(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
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
    Text(text = "TODO")
}

@Composable
private fun IconAdd() {
    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
}

@Preview
@Composable
private fun PreviewLight() {
    PlaygroundTheme {
        ErrorDemo(navigateUp = NOOP)
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNight() {
    PreviewLight()
}
