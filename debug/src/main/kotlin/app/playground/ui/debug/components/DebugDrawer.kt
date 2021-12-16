package app.playground.ui.debug.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DebugDrawer(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    drawer: @Composable ColumnScope.() -> Unit,
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
    ),
    bottomSheet: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {

    val systemLayoutDirection = LocalLayoutDirection.current
    val scope = rememberCoroutineScope()
    CompositionLocalProvider(LocalElevationOverlay provides null) {
        ModalBottomSheetLayout(
            scrimColor = MaterialTheme.colors.primary.copy(alpha = 0.56f),
            sheetState = bottomSheetState,
            sheetShape = MaterialTheme.shapes.large.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp),
            ),
            sheetContent = {
                bottomSheet()
            },
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                ModalDrawer(
                    scrimColor = MaterialTheme.colors.primary.copy(alpha = 0.56f),
                    drawerState = drawerState,
                    drawerContent = {
                        CompositionLocalProvider(
                            LocalLayoutDirection provides systemLayoutDirection,
                            content = { drawer() },
                        )
                    },
                    drawerShape = MaterialTheme.shapes.large.copy(
                        topStart = CornerSize(0.dp),
                        bottomStart = CornerSize(0.dp),
                    ),
                    content = {
                        CompositionLocalProvider(
                            LocalLayoutDirection provides systemLayoutDirection,
                            content = { content() },
                        )
                    },
                )
            }
        }
    }

    BackHandler(bottomSheetState.isVisible) {
        scope.launch {
            bottomSheetState.hide()
        }
    }

    BackHandler(drawerState.isOpen && !bottomSheetState.isVisible) {
        scope.launch {
            drawerState.close()
        }
    }
}
