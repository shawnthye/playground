package feature.playground.producthunt

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ProductHuntNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: ProductHuntDestination = ProductHuntDestination.Feed,
    openDrawer: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        composable(ProductHuntDestination.Feed.route) {
            Feed(openDrawer = openDrawer)
        }
        composable(ProductHuntDestination.Topics.route) {
            Feed(openDrawer = openDrawer)
        }
    }
}
