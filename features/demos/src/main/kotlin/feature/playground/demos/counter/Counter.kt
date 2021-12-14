package feature.playground.demos.counter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
internal fun Counter(navigateUp: NavigateUp) {
    Counter(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
    )
}

@Composable
private fun Counter(viewModel: CounterViewModel, navigateUp: NavigateUp) {
    val uiState by rememberFlowWithLifecycle(viewModel.uiState).collectAsState(CounterUiState(0))

    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = core.playground.ui.R.string.menu_counter,
                navigationUp = navigateUp,
            )
        },
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${uiState.count}", modifier = Modifier.padding(8.dp))
                FloatingActionButton(onClick = { viewModel.onClick() }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        }
    }
}
