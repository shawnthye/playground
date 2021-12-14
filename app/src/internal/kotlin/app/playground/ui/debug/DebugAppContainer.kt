package app.playground.ui.debug

import androidx.compose.runtime.Composable
import app.playground.BuildConfig
import app.playground.ui.AppContainer

object DebugAppContainer : AppContainer {

    @Composable
    override fun Provide(content: @Composable () -> Unit) {
        DebugLayout(
            buildVersionName = BuildConfig.VERSION_NAME,
            buildVersionCode = BuildConfig.VERSION_CODE,
            buildType = BuildConfig.BUILD_TYPE,
        ) { content() }
    }
}
