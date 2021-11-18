package feature.playground.producthunt

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.components.TopAppbar

@Composable
fun Feed(openDrawer: () -> Unit) {
    Scaffold(topBar = { TopAppbar(title = "ProductHunt", navigationClick = openDrawer) }) {
        Text(text = "Product Hunt", modifier = Modifier.statusBarsPadding())
    }
}
