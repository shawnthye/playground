package app.playground.ui.debug

import androidx.activity.compose.BackHandler
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun DebugDrawer(content: @Composable () -> Unit) {

    val drawerState = rememberDrawerState(DrawerValue.Open)
    val scope = rememberCoroutineScope()

    val viewModel: DebugViewModel = viewModel()

    ModalDrawer(drawerState = drawerState, drawerContent = { DebugDrawerMenu(model = viewModel) }) {
        content()
    }

    BackHandler(drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }
}
