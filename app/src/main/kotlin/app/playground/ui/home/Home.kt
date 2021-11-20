package app.playground.ui.home

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
import app.playground.R
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.DrawerAppBar
import core.playground.ui.rememberFlowWithLifecycle

@Composable
fun Home(viewModel: HomeViewModel = hiltViewModel(), navigateUp: NavigateUp) {
    val uiState by rememberFlowWithLifecycle(viewModel.uiState).collectAsState(HomeUiState(0))

    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = R.string.menu_home,
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