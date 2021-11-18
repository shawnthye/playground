package feature.playground.producthunt

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import feature.playground.producthunt.ui.Collections
import feature.playground.producthunt.ui.Discover
import feature.playground.producthunt.ui.Post
import feature.playground.producthunt.ui.Topics
import timber.log.Timber

private const val PRODUCT_HUNT = "product-hunt"

sealed class Screen(val route: String) {
    object Discover : Screen("$PRODUCT_HUNT/discover")
    object Topics : Screen("$PRODUCT_HUNT/topics")
    object Collections : Screen("$PRODUCT_HUNT/collections")
}

internal sealed class Destination(
    private val route: String,
) {

    fun createRoute(screen: Screen) = "${screen.route}/$route"

    object Discover : Destination("discover")
    object Topics : Destination("topics")
    object Collections : Destination("collections")

    object Post : Destination("post/{postId}") {
        fun createRoute(screen: Screen, postId: String) = "${screen.route}/posts/$postId"
    }

    object Topic : Destination("topics/{postId}") {
        fun createRoute(screen: Screen, topicId: String) = "${screen.route}/topics/$topicId"
    }
}

@ExperimentalAnimationApi
@Composable
fun ProductHuntNavGraph(
    navController: NavHostController = rememberAnimatedNavController(),
    openDrawer: () -> Unit,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Discover.route,
        enterTransition = { _, _ -> EnterTransition.None },
        exitTransition = { _, _ -> ExitTransition.None },
        popEnterTransition = { _, _ -> EnterTransition.None },
        popExitTransition = { _, _ -> ExitTransition.None },
    ) {
        addDiscoverScreen(navController, openDrawer)
        addTopicsScreen(navController, openDrawer)
        addCollectionsScreen(navController, openDrawer)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addDiscoverScreen(
    navController: NavHostController,
    openDrawer: () -> Unit,
) {
    val screen = Screen.Discover
    navigation(
        route = screen.route,
        startDestination = Destination.Discover.createRoute(screen),
    ) {
        addDiscover(navController, screen, openDrawer)
        addPost(navController, screen)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addTopicsScreen(
    navController: NavHostController,
    openDrawer: () -> Unit,
) {
    val screen = Screen.Topics
    navigation(
        route = screen.route,
        startDestination = Destination.Topics.createRoute(screen),
    ) {
        addTopics(navController, screen, openDrawer)
        addPost(navController, screen)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCollectionsScreen(
    navController: NavHostController,
    openDrawer: () -> Unit,
) {
    val screen = Screen.Collections
    navigation(
        route = screen.route,
        startDestination = Destination.Collections.createRoute(screen),
    ) {
        addCollections(navController, screen, openDrawer)
        addPost(navController, screen)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addDiscover(
    navController: NavHostController,
    screen: Screen,
    openDrawer: () -> Unit,
) {
    Timber.i(navController.toString())
    composable(Destination.Discover.createRoute(screen)) {
        Discover(openDrawer)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addTopics(
    navController: NavHostController,
    screen: Screen,
    openDrawer: () -> Unit,
) {
    Timber.i(navController.toString())
    composable(Destination.Topics.createRoute(screen)) {
        Topics(openDrawer) {
            navController.navigate(Destination.Post.createRoute(screen))
        }
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCollections(
    navController: NavHostController,
    screen: Screen,
    openDrawer: () -> Unit,
) {
    Timber.i(navController.toString())
    composable(Destination.Collections.createRoute(screen)) {
        Collections(openDrawer)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addPost(
    navController: NavHostController,
    screen: Screen,
) {
    Timber.i(navController.toString())
    composable(
        route = Destination.Post.createRoute(screen),
        // arguments = listOf(
        //     navArgument("postId") {
        //         type = NavType.StringType
        //         nullable = false
        //     },
        // ),
    ) {
        Post()
    }
}
