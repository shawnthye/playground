package app.playground.ui

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
import app.playground.navigation.DrawerMenu
import app.playground.navigation.DrawerScreen
import app.playground.navigation.PlaygroundNavGraph
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun PlaygroundApp() {
    val navController = rememberAnimatedNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentScreen by navController.currentScreenAsState()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            val context = LocalContext.current
            DrawerMenu(selectedScreen = currentScreen) { selected ->
                if (selected is DrawerScreen.DeviantArt) {
                    context.startActivity(
                        Intent(
                            context, feature.playground.deviant.ui.DeviantArt::class.java,
                        ),
                    )
                    return@DrawerMenu
                }

                scope.launch {
                    if (selected !is DrawerScreen.DeviantArt) {
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
        PlaygroundNavGraph(
            navController = navController,
            navigateUp = {
                scope.launch {
                    drawerState.open()
                }
            },
        )
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
private fun NavController.currentScreenAsState(): State<DrawerScreen> {
    val selectedItem = remember { mutableStateOf<DrawerScreen>(DrawerScreen.START) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == DrawerScreen.Home.route } -> {
                    selectedItem.value = DrawerScreen.Home
                }
                destination.hierarchy.any { it.route == DrawerScreen.Theme.route } -> {
                    selectedItem.value = DrawerScreen.Theme
                }
                destination.hierarchy.any { it.route == DrawerScreen.ErrorDemo.route } -> {
                    selectedItem.value = DrawerScreen.ErrorDemo
                }
                destination.hierarchy.any { it.route == DrawerScreen.ProductHunt.route } -> {
                    selectedItem.value = DrawerScreen.ProductHunt
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
