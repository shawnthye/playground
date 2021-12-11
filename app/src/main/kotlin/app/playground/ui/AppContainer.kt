package app.playground.ui

import androidx.compose.runtime.Composable

interface AppContainer {

    @Composable
    fun App()

    companion object {
        val DEFAULT = object : AppContainer {
            @Composable
            override fun App() = PlaygroundApp()
        }
    }
}


