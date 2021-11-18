package feature.playground.producthunt.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.components.TopAppbar

@Composable
fun Topics(openDrawer: () -> Unit) {
    Scaffold(topBar = { TopAppbar(title = "Topics", navigationUp = openDrawer) }) {
        Text(text = "Topics", modifier = Modifier.statusBarsPadding())
    }
}
