package app.playground.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.playground.ui.home.Home
import feature.playground.producthunt.ui.ProductHunt

@Composable
fun PlaygroundNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: PlaygroundDestination = PlaygroundDestination.Home,
    openDrawer: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        composable(PlaygroundDestination.Home.route) {
            Home(openDrawer = openDrawer)
        }
        composable(PlaygroundDestination.Gallery.route) {
            ProductHunt(openDrawer = openDrawer)
        }
    }
}
