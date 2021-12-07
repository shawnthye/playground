package feature.playground.producthunt.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.AppBar

@Composable
fun Post(navigateUp: NavigateUp) {
    Scaffold(topBar = { AppBar(title = "Post", navigationUp = navigateUp) }) {
        Text(text = "Post", modifier = Modifier.statusBarsPadding())
    }
}
