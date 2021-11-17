package app.playground.ui.home

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun Home() {
    Scaffold {
        Text(text = "home", modifier = Modifier.statusBarsPadding())
    }
}
