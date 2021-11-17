package app.playground.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.playground.ui.gallery.Gallery
import app.playground.ui.home.Home

@Composable
fun PlaygroundNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: PlaygroundDestination = PlaygroundDestination.Home,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        composable(PlaygroundDestination.Home.route) {
            Home()
        }
        composable(PlaygroundDestination.Gallery.route) {
            Gallery()
        }
    }
}
