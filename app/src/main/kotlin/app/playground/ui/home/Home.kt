package app.playground.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
        Column {
            TextButton(onClick = { viewModel.onClick() }) {
                Text(text = "home ${uiState.count}")
            }
        }
    }
}
