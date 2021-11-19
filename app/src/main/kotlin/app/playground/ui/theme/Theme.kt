package app.playground.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.playground.R
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.DrawerAppBar

private val NOOP: () -> Unit = {
    // do nothing
}

@Composable
fun Theme(navigateUp: NavigateUp) {

    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = R.string.menu_theme,
                navigationUp = navigateUp,
            )
        },
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column {
                Button(onClick = NOOP) {
                    Text(text = "Button")
                }
                TextButton(onClick = NOOP) {
                    Text(text = "Text Button")
                }
            }

        }
    }
}
