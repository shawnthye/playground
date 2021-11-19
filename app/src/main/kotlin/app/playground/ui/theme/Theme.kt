package app.playground.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.playground.R
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.DrawerAppBar

private val NOOP: () -> Unit = {
    // do nothing
}

@Preview
@Composable
fun Theme(navigateUp: NavigateUp = NOOP) {

    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = R.string.menu_theme,
                navigationUp = navigateUp,
            )
        },
    ) {
        Surface(modifier = Modifier.padding(16.dp)) {
            Column {
                FillRow {
                    Button(onClick = NOOP) {
                        ButtonText()
                    }
                    OutlinedButton(onClick = NOOP) {
                        ButtonText()
                    }

                    OutlinedButton(onClick = NOOP) {
                        ButtonText()
                    }
                }

                FillRow {
                    ExtendedFloatingActionButton(
                        text = { ButtonText() }, onClick = NOOP,
                        icon = { IconAdd() },
                    )
                    FloatingActionButton(onClick = NOOP) { IconAdd() }
                    FloatingActionButton(onClick = NOOP, Modifier.size(40.dp)) { IconAdd() }
                }
            }

        }
    }
}

@Composable
fun FillRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
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
fun ButtonText() {
    Text(text = "Button")
}

@Composable
fun IconAdd() {
    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
}
