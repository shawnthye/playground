package app.playground.ui

import android.content.Context
import android.content.Intent
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import app.playground.ui.PlaygroundDestination.Home
import feature.playground.deviant.ui.DeviantArt

sealed class PlaygroundDestination(val route: String) {
    object Home : PlaygroundDestination("home")
    object ProductHunt : PlaygroundDestination("product-hunt")
    object DeviantArt : PlaygroundDestination("deviantArt")

    companion object {
        fun fromRoute(route: String?): PlaygroundDestination {
            return when (route) {
                Home.route -> Home
                ProductHunt.route -> ProductHunt
                DeviantArt.route -> DeviantArt
                else -> Home
            }
        }
    }
}

class PlaygroundNavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(Home.route) {
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

    val navigateToGallery: () -> Unit = {
        navController.navigate(PlaygroundDestination.ProductHunt.route) {
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
