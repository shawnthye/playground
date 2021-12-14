package feature.playground.demos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import core.playground.ui.theme.PlaygroundTheme
import feature.playground.demos.theme.Theme

class Demos : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(core.playground.ui.R.style.Playground)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                PlaygroundTheme {
                    Theme {
                    }
                }
            }
        }
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, Demos::class.java)
            context.startActivity(starter)
        }
    }
}
