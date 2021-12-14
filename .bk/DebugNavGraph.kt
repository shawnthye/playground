package app.playground.ui.debug

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import core.playground.ui.alias.NavigateUp
import feature.playground.demos.counter.Counter
import feature.playground.demos.error.ui.ErrorDemo
import feature.playground.demos.theme.Theme

internal sealed class DebugScreen(val route: String) {
    object Content : DebugScreen("product-hunt")
    object Counter : DebugScreen("counter")
    object Theme : DebugScreen("theme")
    object DeviantArt : DebugScreen("deviant-art")
    object ErrorDemo : DebugScreen("error-demo")

    companion object {
        val START by lazy { Content }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun DebugNavGraph(
    navController: NavHostController = rememberAnimatedNavController(),
    navigateUp: NavigateUp,
    content: @Composable () -> Unit,
) {

    /**
     * We can't have nested NavHost in compose
     * TODO: find alternative like
     * - BottomSheet(Can BottomSheet has dynamic content?)
     * - New Activity?
     */
    AnimatedNavHost(
        navController = navController,
        startDestination = DebugScreen.START.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        addContentScreen(content)
        addHomeScreen(navigateUp)
        addThemeScreen(navigateUp)
        addErrorDemoScreen(navigateUp)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addHomeScreen(navigateUp: NavigateUp) {
    composable(DebugScreen.Counter.route) {
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
private fun NavGraphBuilder.addContentScreen(content: @Composable () -> Unit) {
    composable(DebugScreen.Content.route) {
        content()
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addErrorDemoScreen(navigateUp: NavigateUp) {
    composable(DebugScreen.ErrorDemo.route) {
        ErrorDemo(navigateUp = navigateUp)
    }
}
