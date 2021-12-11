package app.playground.ui

import androidx.compose.runtime.Composable

interface AppContainer {

    @Composable
    fun Provide(content: @Composable () -> Unit) = content()
}


