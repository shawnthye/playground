package app.playground.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import app.playground.ui.ProductHuntApp
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import core.playground.ui.alias.NavigateUp
import feature.playground.demos.counter.Counter
import feature.playground.demos.error.ui.ErrorDemo
import feature.playground.demos.theme.Theme

internal sealed class DrawerScreen(val route: String) {
    object Home : DrawerScreen("home")
    object Theme : DrawerScreen("theme")
    object ProductHunt : DrawerScreen("product-hunt")
    object DeviantArt : DrawerScreen("deviant-art")
    object ErrorDemo : DrawerScreen("error-demo")

    companion object {
        val START = Home
    }
}

private sealed class DrawerDestination(private val route: String) {

    fun createRoute(screen: DrawerScreen) = "${screen.route}/$route"

    object ProductHunt : DrawerDestination("app")
}

@ExperimentalAnimationApi
@Composable
internal fun PlaygroundNavGraph(
    navController: NavHostController = rememberAnimatedNavController(),
    navigateUp: NavigateUp,
) {

    AnimatedNavHost(
        navController = navController,
        startDestination = DrawerScreen.Home.route,
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
    composable(DrawerScreen.Home.route) {
        Counter(navigateUp = navigateUp)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addThemeScreen(navigateUp: NavigateUp) {
    composable(DrawerScreen.Theme.route) {
        Theme(navigateUp = navigateUp)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addProductHuntScreen(navigateUp: NavigateUp) {
    navigation(
        route = DrawerScreen.ProductHunt.route,
        startDestination = DrawerDestination.ProductHunt.createRoute(DrawerScreen.ProductHunt),
    ) {
        addProductHunt(DrawerScreen.ProductHunt, navigateUp)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addErrorDemoScreen(navigateUp: NavigateUp) {
    composable(DrawerScreen.ErrorDemo.route) {
        ErrorDemo(navigateUp = navigateUp)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addProductHunt(screen: DrawerScreen, navigateUp: NavigateUp) {
    composable(DrawerDestination.ProductHunt.createRoute(screen)) {
        ProductHuntApp(navigateUp = navigateUp)
    }
}
