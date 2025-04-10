package com.arbuzerxxl.vibeshot.features.interests.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arbuzerxxl.vibeshot.features.interests.presentation.InterestsRoute
import kotlinx.serialization.Serializable

@Serializable
object InterestsDestination

fun NavController.navigateToInterests(navOptions: NavOptions? = null) {
    navigate(InterestsDestination, navOptions)
}

fun NavGraphBuilder.interests() {
    composable<InterestsDestination> {
        InterestsRoute()
    }
}