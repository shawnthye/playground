package app.playground.ui.debug

import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import app.playground.ui.debug.components.RtlModalDrawer
import core.playground.ui.alias.NavigateUp
import feature.playground.demos.theme.Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DebugDrawer(
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

    var sheetContent by remember { mutableStateOf(BottomSheetContent.FEATURE_FLAGS) }

    val drawerState = rememberDrawerState(
        DrawerValue.Closed,
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

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            CompositionLocalProvider(LocalElevationOverlay provides null) {
                BottomSheetContent(
                    content = sheetContent,
                    upPress = { scope.launch { bottomSheetState.hide() } },
                )
            }
        },
    ) {
        RtlModalDrawer(
            drawerState = drawerState,
            drawer = {
                DebugSettings(
                    buildVersionName = buildVersionName,
                    buildVersionCode = buildVersionCode,
                    buildType = buildType,
                    model = model,
                    coilModel = coilModel,
                    openBottomSheet = {
                        sheetContent = it
                        scope.launch {
                            bottomSheetState.show()
                        }
                    },
                )
            },
            content = { content() },
        )

    }

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
