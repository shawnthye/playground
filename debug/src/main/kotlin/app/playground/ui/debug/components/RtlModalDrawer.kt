package app.playground.ui.debug.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.launch

@Composable
fun RtlModalDrawer(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    drawer: @Composable ColumnScope.() -> Unit,
    onOpened: () -> Unit,
    onClosed: () -> Unit,
    content: @Composable () -> Unit,
) {

    val systemLayoutDirection = LocalLayoutDirection.current
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalDrawer(
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

    LaunchedEffect(drawerState.isOpen) {
        if (drawerState.isOpen) {
            onOpened()
        }
    }

    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            onClosed()
        }
    }

    BackHandler(drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }
}
