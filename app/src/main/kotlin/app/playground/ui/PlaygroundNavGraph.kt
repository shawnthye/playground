package app.playground.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import app.playground.ui.home.Home
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import feature.playground.producthunt.ProductHunt
import feature.playground.producthunt.Screen

@ExperimentalAnimationApi
@Composable
fun PlaygroundNavGraph(
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: PlaygroundDestination = PlaygroundDestination.Home,
    openDrawer: () -> Unit,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination.route,
        enterTransition = { fadeIn(animationSpec = tween(120)) },
        exitTransition = { fadeOut(animationSpec = tween(120)) },
        popEnterTransition = { fadeIn(animationSpec = tween(120)) },
        popExitTransition = { fadeOut(animationSpec = tween(120)) },
    ) {
        composable(PlaygroundDestination.Home.route) {
            Home(openDrawer = openDrawer)
        }

        navigation(
            route = PlaygroundDestination.ProductHunt.route,
            startDestination = Screen.Discover.route,
        ) {
            composable(Screen.Discover.route) {
                ProductHunt(openDrawer = openDrawer)
            }
        }
    }
}
