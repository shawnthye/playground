package feature.playground.producthunt

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

sealed class ProductHuntDestination(val route: String) {
    object Feed : ProductHuntDestination("feed")
    object Topics : ProductHuntDestination("topics")
    object Collections : ProductHuntDestination("collections")

    companion object {
        fun fromRoute(route: String?): ProductHuntDestination {
            return when (route) {
                Feed.route -> Feed
                Topics.route -> Topics
                Collections.route -> Collections
                else -> Feed
            }
        }
    }
}

class PlaygroundNavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(ProductHuntDestination.Feed.route) {
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
        navController.navigate(ProductHuntDestination.Topics.route) {
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
}
