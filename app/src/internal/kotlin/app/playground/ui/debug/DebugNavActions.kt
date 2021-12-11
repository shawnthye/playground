package app.playground.ui.debug

import android.content.Context
import android.content.Intent
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import feature.playground.deviant.ui.DeviantArt

class DebugNavActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(DebugScreen.Home.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    val navigateToTheme: () -> Unit = {
        navController.navigate(DebugScreen.Theme.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    val navigateToProductHunt: () -> Unit = {
        navController.navigate(DebugScreen.ProductHunt.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    val openDeviantArt: (context: Context) -> Unit = {
        it.startActivity(Intent(it, DeviantArt::class.java))
    }
}
