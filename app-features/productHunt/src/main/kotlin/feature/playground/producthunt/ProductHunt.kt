package feature.playground.producthunt

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalAnimationApi
@Composable
fun ProductHunt(openDrawer: () -> Unit) {
    val navController = rememberAnimatedNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val selectedScreen = currentDestination.let { destination ->
        if (destination == null) {
            Screen.Discover
        } else {
            when {
                destination.hierarchy.any { it.route == Screen.Discover.route } -> {
                    Screen.Discover
                }
                destination.hierarchy.any { it.route == Screen.Topics.route } -> {
                    Screen.Topics
                }
                destination.hierarchy.any { it.route == Screen.Collections.route } -> {
                    Screen.Collections
                }
                else -> Screen.Discover
            }
        }
    }

    val popupDestinationId by navController.popUpDestinationId()

    Scaffold(
        bottomBar = {
            ProductHuntBottomNavigation(
                selectedScreen = selectedScreen,
                onNavigationSelected = { selected ->
                    navController.navigate(selected.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(popupDestinationId) { saveState = true }

                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
            )
        },
    ) {
        ProductHuntNavGraph(navController = navController, openDrawer)
    }
}

@Composable
private fun ProductHuntBottomNavigation(
    selectedScreen: Screen,
    onNavigationSelected: (selected: Screen) -> Unit,
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
                    selectedContentColor = MaterialTheme.colors.primary,
                    icon = { Icon(item.imageVector, contentDescription = null) },
                    label = null,
                    selected = selectedScreen == item.screen,
                    onClick = { onNavigationSelected(item.screen) },
                )
            }
        }
    }
}

@Stable
@Composable
private fun NavController.popUpDestinationId(): State<Int> {
    val popUpDestinationId = remember { mutableStateOf(currentDestination?.id ?: -1) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Discover.route } -> {
                    popUpDestinationId.value = destination.id
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return popUpDestinationId
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
