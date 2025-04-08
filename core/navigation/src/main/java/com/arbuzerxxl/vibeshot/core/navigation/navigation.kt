package com.arbuzerxxl.vibeshot.core.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

// Describe the navigation stack according to the presentation
fun NavHostController.navigateSingleTopTo(route: Any) {
    navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}