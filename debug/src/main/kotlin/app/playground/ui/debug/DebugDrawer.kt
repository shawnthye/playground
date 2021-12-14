package app.playground.ui.debug

import androidx.activity.compose.BackHandler
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun DebugDrawer(
    buildVersionName: String,
    buildVersionCode: Int,
    buildType: String,
    content: @Composable () -> Unit,
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val viewModel: DebugViewModel = viewModel()

    val systemLayoutDirection = LocalLayoutDirection.current

    LayoutDirectionProvider(LayoutDirection.Rtl) {
        ModalDrawer(
            drawerContent = {
                LayoutDirectionProvider(systemLayoutDirection) {
                    DebugSettings(
                        buildVersionName = buildVersionName,
                        buildVersionCode = buildVersionCode,
                        buildType = buildType,
                        model = viewModel,
                    )
                }
            },
            drawerState = drawerState,
        ) {
            LayoutDirectionProvider(systemLayoutDirection) {
                content()
            }
        }
    }


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

@Composable
fun LayoutDirectionProvider(
    direction: LayoutDirection = LocalLayoutDirection.current,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        content()
    }
}
