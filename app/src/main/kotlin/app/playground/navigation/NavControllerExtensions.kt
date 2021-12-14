package app.playground.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph
import app.playground.ui.Screen

internal fun NavController.findChildTopRoutes(): List<String> {
    val routes = mutableListOf<String>()
    val nodes = graph.nodes

    for (i in 0 until nodes.size()) {
        (nodes.get(nodes.keyAt(i)) as? NavGraph)?.startDestinationRoute?.run {
            routes.add(this)
        }
    }

    return routes
}

internal fun NavController.findCurrentTopRoute(screen: Screen): String {
    return findChildTopRoutes().first { route -> route.startsWith(screen.route) }
}
