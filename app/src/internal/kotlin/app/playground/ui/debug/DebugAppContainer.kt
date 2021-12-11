package app.playground.ui.debug

import androidx.compose.runtime.Composable
import app.playground.ui.AppContainer

class DebugAppContainer : AppContainer {

    @Composable
    override fun Provide(content: @Composable () -> Unit) {
        DebugDrawer { content() }
    }
}
