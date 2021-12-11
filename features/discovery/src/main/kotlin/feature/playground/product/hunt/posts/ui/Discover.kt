package feature.playground.product.hunt.posts.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import app.playground.store.database.entities.Post
import coil.compose.rememberImagePainter
import coil.request.repeatCount
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import core.playground.ui.components.DrawerAppBar
import core.playground.ui.rememberFlowWithLifecycle

@Composable
fun Discover(openPost: (postId: String) -> Unit) {
    Discover(
        viewModel = hiltViewModel(),
        openPost = openPost,
    )
}

@Composable
internal fun Discover(
    viewModel: DiscoverViewModel,
    openPost: (postId: String) -> Unit,
) {
    val state by rememberFlowWithLifecycle(viewModel.uiState).collectAsState(DiscoveryUiState.EMPTY)
    Discover(
        state = state,
        onSwipeRefresh = {
            viewModel.onRefresh()
        },
        openPost = openPost,
    )
}

@Composable
internal fun Discover(
    state: DiscoveryUiState,
    onSwipeRefresh: () -> Unit,
    openPost: (postId: String) -> Unit,
) {

    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = core.playground.ui.R.string.menu_discover,
            )
        },
    ) { paddingValues ->
        if (state.refreshing && state.posts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(Modifier.padding())
            }
        } else {
            List(
                refreshing = state.refreshing,
                posts = state.posts,
                onSwipeRefresh = onSwipeRefresh,
                contentPadding = paddingValues,
            ) { postId ->
                openPost(postId)
            }
        }

    }
}

@Composable
fun List(
    contentPadding: PaddingValues,
    refreshing: Boolean,
    posts: List<Post>,
    onSwipeRefresh: () -> Unit,
    openPost: (postId: String) -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = onSwipeRefresh,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
        ) {
            items(posts.size) { position ->

                val post = posts[position]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            openPost(post.postId)
                        }
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = rememberImagePainter(data = post.thumbnailUrl) {
                            repeatCount(0)
                        },
                        contentDescription = post.name,
                        modifier = Modifier
                            .size(56.dp)
                            .padding(end = 8.dp),
                    )

                    Column {
                        Text(text = "No. ${position + 1}, id: ${post.id}")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Name: ${post.name}")
                    }
                }
            }
        }
    }
}


