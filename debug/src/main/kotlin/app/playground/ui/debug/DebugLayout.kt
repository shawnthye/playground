package app.playground.ui.debug

import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import app.playground.ui.debug.components.DebugDrawer
import app.playground.ui.debug.theme.DebugTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DebugLayout(
    buildVersionName: String,
    buildVersionCode: Int,
    buildType: String,
    content: @Composable () -> Unit,
) {
    val model: DebugViewModel = viewModel()
    val coilModel: DebugCoilViewModel = viewModel()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed,
        confirmStateChange = {
            if (it == DrawerValue.Closed) {
                model.seenDrawer()
            }
            if (it == DrawerValue.Open) {
                coilModel.submitAction(CoilUiAction.Refresh)
            }
            true
        },
    )
    DebugTheme {
        DebugDrawer(
            drawerState = drawerState,
            drawer = {
                DebugSettings(
                    buildVersionName = buildVersionName,
                    buildVersionCode = buildVersionCode,
                    buildType = buildType,
                    model = model,
                    coilModel = coilModel,
                    showFeatureFlags = { scope.launch { bottomSheetState.show() } },
                )
            },
            bottomSheetState = bottomSheetState,
            bottomSheet = { DebugFeatureFlags() },
            content = { content() },
        )
    }

    LaunchedEffect(Unit) {
        model.seenDrawer.collectLatest { seen ->
            if (!seen && drawerState.isClosed) {
                delay(400)
                drawerState.open()
            }

        }
    }
}
