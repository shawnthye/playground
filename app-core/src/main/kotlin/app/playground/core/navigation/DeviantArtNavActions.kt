package app.playground.core.navigation

import androidx.navigation.NavController

interface DeviantArtNavActions {
    fun toDeviation(navController: NavController, id: String)
}
