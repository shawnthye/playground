package feature.playground.product.hunt.post.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.AppBar
import core.playground.ui.rememberFlowWithLifecycle
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

    Scaffold(topBar = { AppBar(title = uiState?.name, navigationUp = navigateUp) }) {
        if (refreshing && uiState == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(Modifier.padding())
            }
        } else {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = refreshing),
                onRefresh = { model.refresh() },
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
                ) {
                    Text(
                        text = "${uiState?.id}, ${uiState?.name}",
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}
