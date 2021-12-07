package feature.playground.product.hunt.posts.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.DrawerAppBar

@Composable
fun Discover(navigateUp: NavigateUp, openPost: (postId: String) -> Unit) {
    Discover(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openPost = openPost,
    )
}

@Composable
internal fun Discover(
    viewModel: DiscoverViewModel,
    navigateUp: NavigateUp,
    openPost: (postId: String) -> Unit,
) {
    viewModel.javaClass
    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = core.playground.ui.R.string.menu_discover, navigationUp = navigateUp,
            )
        },
    ) {
        TextButton(onClick = { openPost("post from discover") }) {
            Text(text = "Discover", modifier = Modifier.statusBarsPadding())
        }
    }
}


