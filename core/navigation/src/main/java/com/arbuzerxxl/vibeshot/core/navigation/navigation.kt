package com.arbuzerxxl.vibeshot.core.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.collections.immutable.ImmutableList

data class TopDestinationsCollection(val items: ImmutableList<TopLevelDestination>)

interface TopLevelDestination {
    val icon: ImageVector
    @get:StringRes
    val titleId: Int
    val route: Any
    val destinationRoute: String
}


// Describe the navigation stack according to the presentation
fun NavHostController.navigateSingleTopTo(route: Any) {
    navigate(route) {
        popBackStack()
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true

    }
}