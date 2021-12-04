package app.playground.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import feature.playground.demos.error.ui.ErrorDemo
import feature.playground.demos.counter.Counter
import feature.playground.demos.theme.Theme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import core.playground.ui.alias.NavigateUp
import feature.playground.producthunt.ProductHunt

internal sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Theme : Screen("theme")
    object ProductHunt : Screen("product-hunt")
    object DeviantArt : Screen("deviant-art")
    object ErrorDemo : Screen("error-demo")

    companion object {
        val START = Home
    }
}

private sealed class Destination(private val route: String) {

    fun createRoute(screen: Screen) = "${screen.route}/$route"

    object ProductHunt : Destination("app")
}

@ExperimentalAnimationApi
@Composable
internal fun PlaygroundNavGraph(
    navController: NavHostController = rememberAnimatedNavController(),
    navigateUp: NavigateUp,
) {

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        addHomeScreen(navigateUp)
        addThemeScreen(navigateUp)
        addErrorDemoScreen(navigateUp)
        addProductHuntScreen(navigateUp)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addHomeScreen(navigateUp: NavigateUp) {
    composable(Screen.Home.route) {
        Counter(navigateUp = navigateUp)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addThemeScreen(navigateUp: NavigateUp) {
    composable(Screen.Theme.route) {
        Theme(navigateUp = navigateUp)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addProductHuntScreen(navigateUp: NavigateUp) {
    navigation(
        route = Screen.ProductHunt.route,
        startDestination = Destination.ProductHunt.createRoute(Screen.ProductHunt),
    ) {
        addProductHunt(Screen.ProductHunt, navigateUp)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addErrorDemoScreen(navigateUp: NavigateUp) {
    composable(Screen.ErrorDemo.route) {
        ErrorDemo(navigateUp = navigateUp)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addProductHunt(screen: Screen, navigateUp: NavigateUp) {
    composable(Destination.ProductHunt.createRoute(screen)) {
        ProductHunt(navigateUp = navigateUp)
    }
}
