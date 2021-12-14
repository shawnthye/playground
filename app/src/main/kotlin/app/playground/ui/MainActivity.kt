package app.playground.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import core.playground.ui.theme.PlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var container: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(core.playground.ui.R.style.Playground)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                container.Provide {
                    PlaygroundTheme {
                        PlaygroundApp()
                    }
                }
            }
        }
    }
}
