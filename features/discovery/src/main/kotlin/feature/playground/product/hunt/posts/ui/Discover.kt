package feature.playground.product.hunt.posts.ui

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.playground.store.database.entities.Discovery
import app.playground.store.database.entities.Post
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import core.playground.ui.components.TopAppBar
import core.playground.ui.extension.asGif
import core.playground.ui.rememberFlowWithLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun Discover(openPost: (postId: String) -> Unit) {
    Discover(
        viewModel = hiltViewModel(),
        openPost = openPost,
    )
}

@Composable
private fun Discover(
    viewModel: DiscoverViewModel,
    openPost: (postId: String) -> Unit,
) {
    Discover(
        list = rememberFlowWithLifecycle(viewModel.pagedList).collectAsLazyPagingItems(),
        openPost = openPost,
    )
}

@Composable
private fun Discover(
    list: LazyPagingItems<Discovery>,
    openPost: (postId: String) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(titleRes = core.playground.ui.R.string.menu_discover) },
    ) { paddingValues ->

        if (list.refreshing) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(Modifier.padding())
            }
        } else {
            Posts(
                list = list,
                onSwipeRefresh = { list.refresh() },
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
    list: LazyPagingItems<Discovery>,
    onSwipeRefresh: () -> Unit,
    openPost: (postId: String) -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = list.refreshing),
        onRefresh = onSwipeRefresh,
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            cells = GridCells.Adaptive(128.dp),
        ) {

            items(list.itemCount) { position ->
                PostsItem(post = list[position]!!.post, openPost = openPost)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PostsItem(post: Post, openPost: (postId: String) -> Unit) {

    openPost.let { }

    val interaction = remember { MutableInteractionSource() }

    val pressed by interaction.collectIsPressedAsState()

    Box(
        modifier = Modifier.combinedClickable(
            interactionSource = interaction,
            indication = LocalIndication.current,
            onClick = { openPost(post.postId) },
        ),
    ) {
        Logo(post = post, pressed = pressed)
    }
}

@OptIn(ExperimentalCoilApi::class, ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.Logo(post: Post, pressed: Boolean = false) {

    var animatable by remember { mutableStateOf<Animatable?>(null) }

    val painter = rememberImagePainter(post.thumbnailUrl) {
        scale(Scale.FIT)
    }

    val state = painter.state
    if (state is ImagePainter.State.Success) {
        animatable = state.result.drawable.asGif()
        animatable?.stop()
    }

    Image(
        painter = painter,
        contentDescription = post.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f, true),
    )

    if (animatable != null) {
        GifBadge(modifier = Modifier.align(Alignment.BottomEnd))
    }

    LaunchedEffect(pressed) {
        snapshotFlow { pressed }.distinctUntilChanged().collect {
            if (it) {
                animatable?.start()
            } else {
                animatable?.stop()
                (animatable as? Drawable)?.invalidateSelf()
            }
        }
    }
}

@Composable
private fun GifBadge(modifier: Modifier = Modifier) {
    Surface(
        color = Color.Black.copy(alpha = 0.3f),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(6.dp),
    ) {
        Text(
            text = "GIF",
            style = MaterialTheme.typography.overline,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
        )
    }
}

private val LazyPagingItems<*>.refreshing: Boolean
    get() {
        return loadState.refresh == LoadState.Loading && itemCount == 0
    }
