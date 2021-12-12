package app.playground.ui.debug

import androidx.compose.runtime.Composable
import app.playground.BuildConfig
import app.playground.ui.AppContainer

class DebugAppContainer : AppContainer {

    @Composable
    override fun Provide(content: @Composable () -> Unit) {
        DebugDrawer(
            buildVersionName = BuildConfig.VERSION_NAME,
            buildVersionCode = "${BuildConfig.VERSION_CODE}",
            buildType = BuildConfig.BUILD_TYPE,
        ) { content() }
    }
}
