package feature.playground.product.hunt.posts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.DrawerAppBar
import core.playground.ui.rememberFlowWithLifecycle

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
    val state by rememberFlowWithLifecycle(viewModel.state).collectAsState(DiscoverState.EMPTY)
    Discover(state = state, navigateUp = navigateUp, openPost = openPost)
}

@Composable
internal fun Discover(
    state: DiscoverState,
    navigateUp: NavigateUp,
    openPost: (postId: String) -> Unit,
) {

    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = core.playground.ui.R.string.menu_discover, navigationUp = navigateUp,
            )
        },
    ) { paddingValues ->
        if (state.refreshing && state.posts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(Modifier.padding())
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            items(state.posts.size) { position ->

                val post = state.posts[position]

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            openPost(post.id)
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    Text(text = "No. ${position + 1}, id: ${post.id}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Name: ${post.name}")
                }
            }
        }
    }
}


