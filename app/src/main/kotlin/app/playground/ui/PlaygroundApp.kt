package app.playground.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import app.playground.navigation.findCurrentTopRoute
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun PlaygroundApp() {
    val navController = rememberAnimatedNavController()
    val currentScreen by navController.currentScreenAsState()

    Scaffold(
        bottomBar = {
            PlaygroundBottomNavigation(
                selectedScreen = currentScreen,
                onNavigationSelected = { selected ->
                    when (selected) {
                        currentScreen -> navController.popBackStack(
                            route = navController.findCurrentTopRoute(selected),
                            inclusive = false,
                        )
                        else -> navController.navigateTo(screen = selected)
                    }
                },
            )
        },
    ) { innerPadding ->
        PlaygroundNavGraph(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            onSelectedDefaultScreen = {
                navController.navigateTo(screen = it)
            },
        )
    }
}

private fun NavController.navigateTo(screen: Screen) {
    navigate(screen.route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }

        // Avoid multiple copies of the same destination when
        // re-selecting the same item
        launchSingleTop = true
        // Restore state when re-selecting a previously selected item
        restoreState = true
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
private fun NavController.currentScreenAsState(): State<Screen> {
    val selectedItem = remember { mutableStateOf<Screen>(Screen.START) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Discover.route } -> {
                    selectedItem.value = Screen.Discover
                }
                destination.hierarchy.any { it.route == Screen.Topics.route } -> {
                    selectedItem.value = Screen.Topics
                }
                destination.hierarchy.any { it.route == Screen.Collections.route } -> {
                    selectedItem.value = Screen.Collections
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

@Composable
private fun PlaygroundBottomNavigation(
    selectedScreen: Screen,
    onNavigationSelected: (selected: Screen) -> Unit,
) {
    CompositionLocalProvider(
        LocalElevationOverlay provides null,
    ) {
        Surface(
            color = MaterialTheme.colors.surface,
            elevation = AppBarDefaults.BottomAppBarElevation,
        ) {
            BottomNavigation(
                modifier = Modifier.navigationBarsPadding(),
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
            ) {
                ProductHuntNavigationItems.map { item ->
                    BottomNavigationItem(
                        selectedContentColor = colorResource(
                            id = core.playground.ui.R.color.brandProductHunt,
                        ),
                        unselectedContentColor = MaterialTheme.colors.onSurface,
                        icon = { Icon(item.imageVector, contentDescription = null) },
                        label = null,
                        selected = selectedScreen == item.screen,
                        onClick = { onNavigationSelected(item.screen) },
                    )
                }
            }
            if (!MaterialTheme.colors.isLight) {
                Divider(thickness = 0.6.dp)
            }
        }
    }
}

private data class ProductHuntNavigationItem(
    val screen: Screen,
    val imageVector: ImageVector,
)

private val ProductHuntNavigationItems = listOf(
    ProductHuntNavigationItem(Screen.Discover, imageVector = Icons.Filled.Search),
    ProductHuntNavigationItem(Screen.Topics, imageVector = Icons.Filled.Info),
    ProductHuntNavigationItem(Screen.Collections, imageVector = Icons.Filled.List),
)
