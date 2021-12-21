package feature.playground.product.hunt.post.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.AppBar
import core.playground.ui.rememberFlowWithLifecycle
import core.playground.ui.string
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun Post(navigateUp: NavigateUp) {
    Post(hiltViewModel(), navigateUp)
}

@Composable
internal fun Post(model: PostViewModel = hiltViewModel(), navigateUp: NavigateUp) {

    val uiState by rememberFlowWithLifecycle(model.uiState).collectAsState(null)

    val refreshing by model.refreshing.collectAsState(true)

    Timber.i("refreshing:$refreshing, uiState: $uiState")

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(title = uiState?.name, navigationUp = navigateUp) },
    ) {
        if (refreshing && uiState == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(Modifier.padding())
            }
        } else {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = false),
                swipeEnabled = !refreshing,
                onRefresh = { model.refresh() },
                indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = trigger,
                        scale = true,
                        backgroundColor = MaterialTheme.colors.onSurface,
                        contentColor = MaterialTheme.colors.surface,
                    )
                },
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
                ) {
                    Image(
                        painter = rememberImagePainter(data = uiState?.thumbnailUrl),
                        contentDescription = uiState?.name,
                        modifier = Modifier.aspectRatio(16 / 9f),
                    )

                    Text(
                        text = "${uiState?.id}, ${uiState?.name}",
                        modifier = Modifier.padding(16.dp),
                    )
                }

                AnimatedVisibility(
                    visible = refreshing && uiState != null,
                    enter = fadeIn(tween(250)),
                    exit = fadeOut(tween(250)),
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp),
                    )
                }
            }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(model.snackBarMessage) {
        model.snackBarMessage.collectLatest {
            scaffoldState.snackbarHostState.showSnackbar(it.string(context))
        }
    }
}
