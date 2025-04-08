package com.arbuzerxxl.vibeshot.features.start.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arbuzerxxl.vibeshot.features.start.StartRoute
import kotlinx.serialization.Serializable

@Serializable
data object StartRoute

fun NavController.navigateToStart(navOptions: NavOptions? = null) {
    navigate(StartRoute, navOptions)
}

fun NavGraphBuilder.start() {
    composable<StartRoute> {
        StartRoute()
    }
}