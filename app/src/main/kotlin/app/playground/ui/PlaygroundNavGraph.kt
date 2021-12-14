package app.playground.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import app.playground.navigation.findChildTopRoutes
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import feature.playground.product.hunt.post.ui.Post
import feature.playground.product.hunt.posts.ui.Discover
import feature.playground.producthunt.ui.Collections
import feature.playground.producthunt.ui.Topics
import timber.log.Timber

/**
 * This match the root drawer route
 */
private const val PREFIX = "app"

internal sealed class Screen(val route: String) {
    object Discover : Screen("$PREFIX/discover")
    object Topics : Screen("$PREFIX/topics")
    object Collections : Screen("$PREFIX/collections")

    companion object {
        val START by lazy { Discover }
    }
}

internal sealed class Destination(
    private val route: String,
) {

    fun createRoute(screen: Screen) = "${screen.route}/$route"

    object Discover : Destination("discover")
    object Topics : Destination("topics")
    object Collections : Destination("collections")

    object Post : Destination("posts/{postId}") {
        fun createRoute(screen: Screen, postId: String) = "${screen.route}/posts/$postId"
    }

    object Topic : Destination("topics/{topicId}") {
        fun createRoute(screen: Screen, topicId: String) = "${screen.route}/topics/$topicId"
    }
}

@ExperimentalAnimationApi
@Composable
internal fun PlaygroundNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onSelectedDefaultScreen: (Screen) -> Unit,
) {

    val shouldControlBack by navController.shouldControlBack(default = Screen.Discover)

    AnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Discover.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        addDiscoverScreen(navController)
        addTopicsScreen(navController)
        addCollectionsScreen(navController)
    }

    BackHandler(enabled = shouldControlBack) {
        onSelectedDefaultScreen(Screen.START)
    }
}

@Stable
@Composable
private fun NavController.shouldControlBack(default: Screen): State<Boolean> {
    val state = remember { mutableStateOf(false) }

    var defaultAtTopLevel by remember { mutableStateOf(true) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val route = destination.route
            val tops = findChildTopRoutes()

            if (destination.hierarchy.any { it.route == default.route }) {
                defaultAtTopLevel = when {
                    tops.any { it == route } -> {
                        true
                    }
                    else -> false
                }
            }

            val shouldControl = when {
                destination.hierarchy.any {
                    it.route == default.route
                } && defaultAtTopLevel -> false
                tops.any { it == route } -> true
                else -> false
            }

            Timber.i("defaultAtTopLevel: $defaultAtTopLevel")
            Timber.i("shouldControl: $shouldControl")

            state.value = shouldControl
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return state
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addDiscoverScreen(
    navController: NavHostController,
) {
    val screen = Screen.Discover
    navigation(
        route = screen.route,
        startDestination = Destination.Discover.createRoute(screen),
    ) {
        addDiscover(navController, screen)
        addPost(navController, screen)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addTopicsScreen(
    navController: NavHostController,
) {
    val screen = Screen.Topics
    navigation(
        route = screen.route,
        startDestination = Destination.Topics.createRoute(screen),
    ) {
        addTopics(navController, screen)
        addPost(navController, screen)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCollectionsScreen(
    navController: NavHostController,
) {
    val screen = Screen.Collections
    navigation(
        route = screen.route,
        startDestination = Destination.Collections.createRoute(screen),
    ) {
        addCollections(navController, screen)
        addPost(navController, screen)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addDiscover(
    navController: NavHostController,
    screen: Screen,
) {
    composable(Destination.Discover.createRoute(screen)) {
        Discover { postId ->
            navController.navigate(Destination.Post.createRoute(screen, postId))
        }
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addTopics(
    navController: NavHostController,
    screen: Screen,
) {
    navController.toString()
    composable(Destination.Topics.createRoute(screen)) {
        Topics { postId ->
            navController.navigate(Destination.Post.createRoute(screen, postId))
        }
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCollections(
    navController: NavHostController,
    screen: Screen,
) {
    navController.toString()
    composable(Destination.Collections.createRoute(screen)) {
        Collections()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addPost(
    navController: NavHostController,
    screen: Screen,
) {
    navController.toString()
    composable(
        route = Destination.Post.createRoute(screen),
        arguments = listOf(
            navArgument("postId") {
                type = NavType.StringType
                nullable = false
            },
        ),
    ) {
        Post(navigateUp = navController::navigateUp)
    }
}
