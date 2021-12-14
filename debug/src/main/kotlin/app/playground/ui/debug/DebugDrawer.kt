package app.playground.ui.debug

import androidx.compose.material.DrawerValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import app.playground.ui.debug.components.RtlModalDrawer
import kotlinx.coroutines.delay

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

    RtlModalDrawer(
        drawerState = drawerState,
        drawer = {
            DebugSettings(
                buildVersionName = buildVersionName,
                buildVersionCode = buildVersionCode,
                buildType = buildType,
                model = model,
                coilModel = coilModel,
            )
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
