package app.playground.ui.debug

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination

import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import feature.playground.deviant.ui.DeviantArt
import kotlinx.coroutines.launch

@Composable
fun DebugDrawer(content: @Composable () -> Unit) {

    @OptIn(ExperimentalAnimationApi::class)
    val navController = rememberAnimatedNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentScreen by navController.currentScreenAsState()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            val context = LocalContext.current
            DebugDrawerMenu(selectedScreen = currentScreen) { selected ->
                if (selected is DebugScreen.DeviantArt) {
                    context.startActivity(
                        Intent(
                            context, DeviantArt::class.java,
                        ),
                    )
                    return@DebugDrawerMenu
                }

                scope.launch {
                    if (selected !is DebugScreen.DeviantArt) {
                        drawerState.close()
                    }

                    if (currentScreen == selected) {
                        return@launch
                    }

                    navController.navigate(selected.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                }
            }
        },
    ) {
        DebugNavGraph(
            navController = navController,
            navigateUp = {
                scope.launch {
                    drawerState.open()
                }
            },
        ) {
            content()
        }
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
private fun NavController.currentScreenAsState(): State<DebugScreen> {
    val selectedItem = remember { mutableStateOf<DebugScreen>(DebugScreen.START) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == DebugScreen.Counter.route } -> {
                    selectedItem.value = DebugScreen.Counter
                }
                destination.hierarchy.any { it.route == DebugScreen.Theme.route } -> {
                    selectedItem.value = DebugScreen.Theme
                }
                destination.hierarchy.any { it.route == DebugScreen.ErrorDemo.route } -> {
                    selectedItem.value = DebugScreen.ErrorDemo
                }
                destination.hierarchy.any { it.route == DebugScreen.Content.route } -> {
                    selectedItem.value = DebugScreen.Content
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}
