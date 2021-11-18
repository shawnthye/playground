package feature.playground.producthunt.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.components.TopAppbar

@Composable
fun Post() {
    Scaffold(topBar = { TopAppbar(title = "Post", navigationUp = null) }) {
        Text(text = "Post", modifier = Modifier.statusBarsPadding())
    }
}
