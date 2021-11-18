package feature.playground.producthunt

// class PlaygroundNavigationActions(navController: NavHostController) {
//     val navigateToHome: () -> Unit = {
//         navController.navigate(Destination.Discover.createRoute()) {
//             // Pop up to the start destination of the graph to
//             // avoid building up a large stack of destinations
//             // on the back stack as users select items
//             popUpTo(navController.graph.findStartDestination().id) {
//                 saveState = true
//             }
//             // Avoid multiple copies of the same destination when
//             // reselecting the same item
//             launchSingleTop = true
//             // Restore state when reselecting a previously selected item
//             restoreState = true
//         }
//     }
//
//     val navigateToGallery: () -> Unit = {
//         navController.navigate(Destination.Topics.createRoute()) {
//             // Pop up to the start destination of the graph to
//             // avoid building up a large stack of destinations
//             // on the back stack as users select items
//             popUpTo(navController.graph.findStartDestination().id) {
//                 saveState = true
//             }
//             // Avoid multiple copies of the same destination when
//             // reselecting the same item
//             launchSingleTop = true
//             // Restore state when reselecting a previously selected item
//             restoreState = true
//         }
//     }
// }
