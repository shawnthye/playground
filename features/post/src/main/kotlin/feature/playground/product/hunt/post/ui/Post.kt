package feature.playground.product.hunt.post.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.AppBar
import core.playground.ui.rememberFlowWithLifecycle

@Composable
fun Post(navigateUp: NavigateUp) {
    Post(hiltViewModel(), navigateUp)
}

@Composable
internal fun Post(viewModel: PostViewModel = hiltViewModel(), navigateUp: NavigateUp) {

    val state by rememberFlowWithLifecycle(viewModel.state).collectAsState(initial = null)

    Scaffold(topBar = { AppBar(title = "Post", navigationUp = navigateUp) }) {
        Text(text = "${viewModel.id}, ${state?.name}", modifier = Modifier.statusBarsPadding())
    }
}
