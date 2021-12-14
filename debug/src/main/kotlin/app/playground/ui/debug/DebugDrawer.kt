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
import timber.log.Timber

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

    val drawerState = rememberDrawerState(DrawerValue.Closed)

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
        onOpened = { coilModel.submitAction(CoilAction.Refresh) },
        onClosed = { model.seenDrawer() },
        content = { content() },
    )

    LaunchedEffect(seenDrawer) {
        Timber.i("$seenDrawer")
        if (!seenDrawer && drawerState.isClosed) {
            delay(400)
            drawerState.open()
        }
    }
}
