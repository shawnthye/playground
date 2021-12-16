package feature.playground.demos

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import core.playground.ui.theme.Icons
import feature.playground.demos.counter.Counter
import feature.playground.demos.error.ui.ErrorDemo
import feature.playground.demos.theme.ThemeDemo

internal enum class DemoScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    THEMES("themes", "Themes", Icons.Dashboard),
    ERRORS("errors", "Error", Icons.PriorityHigh),
    COUNTER("counter", "Counter", Icons.Calculate);

    companion object {
        val Start by lazy { THEMES }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun DemoNavigation(
    navController: NavHostController,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = DemoScreen.Start.route,
        enterTransition = { EnterTransition.None },
        popEnterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        addStart()
        addErrors()
        addCounter()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addStart() {
    composable(DemoScreen.THEMES.route) {
        ThemeDemo()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addErrors() {
    composable(DemoScreen.ERRORS.route) {
        ErrorDemo()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCounter() {
    composable(DemoScreen.COUNTER.route) {
        Counter()
    }
}
