package feature.playground.producthunt.ui

import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.components.TopAppBar
import feature.playground.producthunt.R

@Composable
fun Topics(openPost: (postId: String) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(titleRes = R.string.menu_topics) },
    ) {
        Button(onClick = { openPost("postid from topics") }) {
            Text(text = "Topics", modifier = Modifier.statusBarsPadding())
        }
    }
}
