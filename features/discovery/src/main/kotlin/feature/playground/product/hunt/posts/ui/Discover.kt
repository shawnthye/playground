package feature.playground.product.hunt.posts.ui

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import core.playground.ui.UiMessage
import core.playground.ui.asUiMessageOr
import core.playground.ui.components.TopAppBar
import core.playground.ui.extension.asGif
import core.playground.ui.rememberFlowWithLifecycle
import core.playground.ui.string
import core.playground.ui.theme.contentPadding
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

        when {
            list.refreshing -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(Modifier.padding())
                }
            }
            list.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = list.error!!.string(LocalContext.current),
                            modifier = Modifier.contentPadding(),
                        )
                        TextButton(onClick = { list.refresh() }) {
                            Text(
                                text = stringResource(
                                    id = core.playground.ui.R.string.action_retry,
                                ).uppercase(),
                            )
                        }
                    }
                }
            }
            else -> {
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
        val background = Color.Black.compositeOver(MaterialTheme.colors.onSurface)
        val placeholders = remember(background) {
            arrayOf(
                background.copy(alpha = 0.40f),
                background.copy(alpha = 0.30f),
                background.copy(alpha = 0.20f),
                Color.Transparent,
            )
        }

        BoxWithConstraints {
            val nColumns = maxOf((maxWidth / 128.dp).toInt(), 1)
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                cells = GridCells.Fixed(nColumns),
                // horizontalArrangement = Arrangement.spacedBy(3.dp),
                // verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {

                items(list.itemCount) { position ->

                    val placeholder = remember { placeholders[position % placeholders.size] }

                    val post = list[position]?.post

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(placeholder)
                            .padding(1.dp),
                    ) {
                        if (post != null) {
                            val state = remember(post.postId) { post }
                            PostsItem(
                                post = state,
                                placeholder = placeholder,
                                openPost = openPost,
                            )
                        }
                    }
                }

                loadingBar(nColumns = nColumns, list = list)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyGridScope.loadingBar(nColumns: Int, list: LazyPagingItems<*>) {
    if (list.itemCount == 0 || list.loadState.append.endOfPaginationReached) {
        return
    }

    items(maxOf(nColumns - (list.itemCount % nColumns), 0)) {
        // render an empty item to fill the grid span
        Spacer(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth(),
        )
    }

    item(span = { GridItemSpan(nColumns) }) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PostsItem(
    post: Post,
    placeholder: Color,
    openPost: (postId: String) -> Unit,
) {

    val interaction = remember { MutableInteractionSource() }

    val pressed by interaction.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .combinedClickable(
                interactionSource = interaction,
                indication = LocalIndication.current,
                onClick = { openPost(post.postId) },
                onDoubleClick = { /* NO-OP */ },
            )
            .background(placeholder),
    ) {
        Logo(post = post, pressed = pressed)
    }
}

@OptIn(ExperimentalCoilApi::class, ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.Logo(
    post: Post,
    pressed: Boolean = false,
) {

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
        color = Color.Black.copy(alpha = 0.35f),
        shape = MaterialTheme.shapes.small,
        modifier = modifier.padding(6.dp),
    ) {
        Text(
            text = "GIF",
            style = MaterialTheme.typography.overline,
            color = Color.White.copy(alpha = 0.58f),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
        )
    }
}

private val LazyPagingItems<*>.refreshing: Boolean
    get() {
        return itemCount == 0 && loadState.refresh == LoadState.Loading
    }

private val LazyPagingItems<*>.error: UiMessage?
    get() {
        if (itemCount == 0 && loadState.refresh is LoadState.Error) {
            return (loadState.refresh as LoadState.Error).error.asUiMessageOr {
                when (it.code) {
                    429 -> UiMessage.String("Rate limit reached, Please try again later")
                    else -> UiMessage.String(it.message)
                }
            }
        }
        return null
    }
