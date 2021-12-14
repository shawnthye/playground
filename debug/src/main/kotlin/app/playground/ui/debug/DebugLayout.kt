package app.playground.ui.debug

import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import app.playground.ui.debug.components.DebugDrawer
import core.playground.ui.alias.NavigateUp
import feature.playground.demos.theme.Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal typealias BottomSheetView = @Composable (NavigateUp) -> Unit

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
    val seenDrawer by model.seenDrawer.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    var bottomSheet: BottomSheetView by remember { mutableStateOf({ Theme(it) }) }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed,
        confirmStateChange = {
            if (it == DrawerValue.Closed) {
                model.seenDrawer()
            }
            if (it == DrawerValue.Open) {
                coilModel.submitAction(CoilAction.Refresh)
            }
            true
        },
    )


    DebugDrawer(
        drawerState = drawerState,
        drawer = {
            DebugSettings(
                buildVersionName = buildVersionName,
                buildVersionCode = buildVersionCode,
                buildType = buildType,
                model = model,
                coilModel = coilModel,
                openBottomSheet = {
                    bottomSheet = it
                    scope.launch {
                        bottomSheetState.show()
                    }
                },
            )
        },
        bottomSheetState = bottomSheetState,
        bottomSheet = {
            bottomSheet {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        },
        content = { content() },
    )

    LaunchedEffect(seenDrawer) {
        if (!seenDrawer && drawerState.isClosed) {
            delay(400)
            drawerState.open()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheetContent(
    content: BottomSheetContent,
    upPress: NavigateUp,
) {

    when (content) {
        BottomSheetContent.FEATURE_FLAGS -> DebugFeatureFlags()
        BottomSheetContent.DEMOS -> Theme {
            upPress()
        }
    }
}
