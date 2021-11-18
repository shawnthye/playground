package feature.playground.producthunt.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.components.TopAppbar

@Composable
fun Discover(openDrawer: () -> Unit) {
    Scaffold(topBar = { TopAppbar(title = "ProductHunt", navigationUp = openDrawer) }) {
        Text(text = "Discover", modifier = Modifier.statusBarsPadding())
    }
}
