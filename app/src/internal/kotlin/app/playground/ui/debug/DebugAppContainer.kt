package app.playground.ui.debug

import androidx.compose.runtime.Composable
import app.playground.ui.AppContainer

object DebugAppContainer : AppContainer {
    @Composable
    override fun App() = DebugDrawer()
}
