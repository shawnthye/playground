package app.playground.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import app.playground.ui.home.Home
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import feature.playground.producthunt.Screen
import feature.playground.producthunt.ProductHunt

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
        enterTransition = { _, _ -> EnterTransition.None },
        exitTransition = { _, _ -> ExitTransition.None },
        popEnterTransition = { _, _ -> EnterTransition.None },
        popExitTransition = { _, _ -> ExitTransition.None },
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
