package app.playground.ui.debug

import androidx.activity.compose.BackHandler
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun DebugDrawer(
    buildVersionName: String,
    buildVersionCode: String,
    buildType: String,
    content: @Composable () -> Unit,
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val viewModel: DebugViewModel = viewModel()

    ModalDrawer(
        drawerContent = {
            DebugSettings(
                buildVersionName = buildVersionName,
                buildVersionCode = buildVersionCode,
                buildType = buildType,
                model = viewModel,
            )
        },
        drawerState = drawerState,
    ) { content() }

    LaunchedEffect(drawerState.isOpen) {
        if (drawerState.isOpen) {
            viewModel.coilRefreshStats()
        }
    }

    BackHandler(drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }
}
