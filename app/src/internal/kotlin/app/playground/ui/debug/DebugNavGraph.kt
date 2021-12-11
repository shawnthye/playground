package app.playground.ui.debug

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import app.playground.ui.PlaygroundApp
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import core.playground.ui.alias.NavigateUp
import feature.playground.demos.counter.Counter
import feature.playground.demos.error.ui.ErrorDemo
import feature.playground.demos.theme.Theme

internal sealed class DebugScreen(val route: String) {
    object ProductHunt : DebugScreen("product-hunt")
    object Home : DebugScreen("home")
    object Theme : DebugScreen("theme")
    object DeviantArt : DebugScreen("deviant-art")
    object ErrorDemo : DebugScreen("error-demo")

    companion object {
        val START by lazy { ProductHunt }
    }
}

private sealed class DrawerDestination(private val route: String) {

    fun createRoute(screen: DebugScreen) = "${screen.route}/$route"

    object ProductHunt : DrawerDestination("app")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun DebugNavGraph(
    navController: NavHostController = rememberAnimatedNavController(),
    navigateUp: NavigateUp,
) {

    AnimatedNavHost(
        navController = navController,
        startDestination = DebugScreen.START.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        addProductHuntScreen()
        addHomeScreen(navigateUp)
        addThemeScreen(navigateUp)
        addErrorDemoScreen(navigateUp)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addHomeScreen(navigateUp: NavigateUp) {
    composable(DebugScreen.Home.route) {
        Counter(navigateUp = navigateUp)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addThemeScreen(navigateUp: NavigateUp) {
    composable(DebugScreen.Theme.route) {
        Theme(navigateUp = navigateUp)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addProductHuntScreen() {
    navigation(
        route = DebugScreen.ProductHunt.route,
        startDestination = DrawerDestination.ProductHunt.createRoute(DebugScreen.ProductHunt),
    ) {
        addProductHunt(DebugScreen.ProductHunt)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addErrorDemoScreen(navigateUp: NavigateUp) {
    composable(DebugScreen.ErrorDemo.route) {
        ErrorDemo(navigateUp = navigateUp)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addProductHunt(screen: DebugScreen) {
    composable(DrawerDestination.ProductHunt.createRoute(screen)) {
        PlaygroundApp()
    }
}
