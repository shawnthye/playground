package feature.playground.product.hunt.posts.ui

import android.graphics.drawable.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.playground.store.database.entities.Post
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.repeatCount
import coil.size.Scale
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import core.playground.ui.components.TopAppBar
import core.playground.ui.extension.asGif
import core.playground.ui.rememberFlowWithLifecycle
import core.playground.ui.tappable

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
        topBar = { TopAppBar(titleRes = core.playground.ui.R.string.menu_discover) },
    ) { paddingValues ->
        if (state.refreshing && state.posts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(Modifier.padding())
            }
        } else {
            Posts(
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Posts(
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
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            cells = GridCells.Adaptive(128.dp),
        ) {
            items(posts) { post ->
                PostsItem(post = post, openPost = openPost)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun PostsItem(post: Post, openPost: (postId: String) -> Unit) {
    var animatable by remember { mutableStateOf<Animatable?>(null) }

    val painter = rememberImagePainter(post.thumbnailUrl) {
        scale(Scale.FIT)
        repeatCount(0)
    }

    val state = painter.state
    if (state is ImagePainter.State.Success) {
        animatable = state.result.drawable.asGif()
    }

    Image(
        painter = painter,
        contentDescription = post.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f, true)
            .tappable(onLongTap = { animatable?.start() }) { openPost(post.postId) },
    )

    LaunchedEffect(animatable) {
        animatable?.stop()
    }
}
