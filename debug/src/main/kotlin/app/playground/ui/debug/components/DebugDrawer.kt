package app.playground.ui.debug.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
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
            sheetBackgroundColor = Color.Transparent,
            sheetContentColor = contentColorFor(backgroundColor = MaterialTheme.colors.surface),
            sheetState = bottomSheetState,
            sheetContent = {
                bottomSheet()
            },
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                ModalDrawer(
                    // drawerBackgroundColor = MaterialTheme.colors.surface,
                    drawerState = drawerState,
                    drawerContent = {
                        CompositionLocalProvider(LocalLayoutDirection provides systemLayoutDirection) {
                            drawer()
                        }
                    },
                    content = {
                        CompositionLocalProvider(LocalLayoutDirection provides systemLayoutDirection) {
                            content()
                        }
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
