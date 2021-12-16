package feature.playground.demos

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
internal fun DemoApp() {
    val menuState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val navController = rememberAnimatedNavController()
    val currentScreen by navController.currentScreenAsState()

    Scaffold(
        bottomBar = {
            DemoAppBar(
                expanded = menuState.isVisible,
                navigateUp = { scope.launch { menuState.navigateUp() } },
            )
        },
    ) { innerPadding ->
        BottomSheetMenu(
            modifier = Modifier.padding(innerPadding),
            state = menuState,
            selected = currentScreen,
            onMenuSelected = {
                navController.navigateTo(it)
                scope.launch {
                    menuState.hide()
                }
            },
        ) {
            DemoNavigation(navController = navController)
        }
    }

    BackHandler(menuState.isVisible) {
        scope.launch { menuState.hide() }
    }

    LaunchedEffect(menuState.isVisible) {
        navController.enableOnBackPressed(!menuState.isVisible)
    }
}

@OptIn(ExperimentalMaterialApi::class)
private suspend fun ModalBottomSheetState.navigateUp() {
    when (currentValue) {
        ModalBottomSheetValue.Expanded -> hide()
        ModalBottomSheetValue.HalfExpanded -> animateTo(ModalBottomSheetValue.Expanded)
        ModalBottomSheetValue.Hidden -> show()
    }
}

private fun NavController.navigateTo(screen: DemoScreen) {
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

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<DemoScreen> {
    val selectedItem = remember { mutableStateOf<DemoScreen>(DemoScreen.Start) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == DemoScreen.THEMES.route } -> {
                    selectedItem.value = DemoScreen.THEMES
                }
                destination.hierarchy.any { it.route == DemoScreen.ERRORS.route } -> {
                    selectedItem.value = DemoScreen.ERRORS
                }
                destination.hierarchy.any { it.route == DemoScreen.COUNTER.route } -> {
                    selectedItem.value = DemoScreen.COUNTER
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
