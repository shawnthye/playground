package app.playground.ui.debug

import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import app.playground.ui.debug.components.DebugDrawer
import app.playground.ui.debug.theme.DebugTheme
import core.playground.ui.rememberFlowWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DebugLayout(content: @Composable () -> Unit) {
    val model: DebugViewModel = viewModel()
    val coilModel: DebugCoilViewModel = viewModel()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
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
                    model = model,
                    coilModel = coilModel,
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
                delay(400.toDuration(DurationUnit.MILLISECONDS))
                drawerState.open()
            }
        }
    }

    val navigationActions = rememberFlowWithLifecycle(model.navigationActions)
    LaunchedEffect(navigationActions) {
        navigationActions.collect {
            when (it) {
                DebugNavigationAction.ShowFeatureFlags -> bottomSheetState.show()
            }
        }
    }
}
