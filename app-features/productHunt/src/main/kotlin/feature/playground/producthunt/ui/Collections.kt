package feature.playground.producthunt.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.components.TopAppbar

@Composable
fun Collections(openDrawer: () -> Unit) {
    Scaffold(topBar = { TopAppbar(title = "Collections", navigationUp = openDrawer) }) {
        Text(text = "Collections", modifier = Modifier.statusBarsPadding())
    }
}
