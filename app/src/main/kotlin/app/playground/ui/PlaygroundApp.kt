package app.playground.ui

import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.playground.navigation.DrawerMenus
import app.playground.ui.PlaygroundDestination.DeviantArt
import app.playground.ui.PlaygroundDestination.Gallery
import app.playground.ui.PlaygroundDestination.Home
import com.google.accompanist.insets.ProvideWindowInsets
import core.playground.ui.theme.PlaygroundTheme
import kotlinx.coroutines.launch

@Composable
internal fun PlaygroundApp() {
    val navController = rememberNavController()
    val navigationActions = remember(navController) { PlaygroundNavigationActions(navController) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = PlaygroundDestination.fromRoute(navBackStackEntry?.destination?.route)

    PlaygroundTheme {
        ProvideWindowInsets {
            ModalDrawer(
                drawerState = drawerState,
                drawerContent = {
                    val context = LocalContext.current
                    DrawerMenus(currentDestination) {
                        when (it) {
                            Home -> navigationActions.navigateToHome()
                            Gallery -> navigationActions.navigateToGallery()
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
