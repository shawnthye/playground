package app.playground.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import core.playground.ui.theme.PlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(core.playground.ui.R.style.Playground)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            /**
             * We initial set the [[LocalElevationOverlay] provides null] to
             * disable surface overlay color, but we can actually do it in our custom app bar
             * We keep this, so in the future we can set DateFormatProvider or etc :)
             */
            CompositionLocalProvider {
                ProvideWindowInsets(consumeWindowInsets = false) {
                    PlaygroundTheme {
                        PlaygroundApp()
                    }
                }
            }
        }
    }
}
