package app.playground.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import app.playground.navigation.DrawerMenus
import app.playground.ui.PlaygroundDestination.DeviantArt
import app.playground.ui.PlaygroundDestination.Home
import app.playground.ui.PlaygroundDestination.ProductHunt
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import core.playground.ui.theme.PlaygroundTheme
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
internal fun PlaygroundApp() {
    val navController = rememberAnimatedNavController()
    val navigationActions = remember(navController) { PlaygroundNavigationActions(navController) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination.let { destination ->
        if (destination == null) {
            Home
        } else {
            when {
                destination.hierarchy.any { it.route == Home.route } -> Home
                destination.hierarchy.any { it.route == ProductHunt.route } -> ProductHunt
                else -> Home
            }
        }
    }

    PlaygroundTheme {
        ProvideWindowInsets {
            ModalDrawer(
                drawerState = drawerState,
                drawerContent = {
                    val context = LocalContext.current
                    DrawerMenus(currentDestination) {
                        when (it) {
                            Home -> navigationActions.navigateToHome()
                            ProductHunt -> navigationActions.navigateToGallery()
                            DeviantArt -> navigationActions.openDeviantArt(context)
                        }

                        scope.launch {
                            if (it !is DeviantArt) {
                                drawerState.close()
                            }
                        }
                    }
                },
            ) {
                PlaygroundNavGraph(
                    navController = navController,
                    openDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                )
            }
        }
    }
}
