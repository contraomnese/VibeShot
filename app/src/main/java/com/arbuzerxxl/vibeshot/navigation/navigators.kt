package com.arbuzerxxl.vibeshot.navigation

import androidx.navigation.NavHostController
import com.arbuzerxxl.vibeshot.features.auth.navigation.AuthNavigator
import com.arbuzerxxl.vibeshot.features.auth.navigation.navigateToAuth
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.BottomMenuNavigator
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.navigateToBottomMenu
import com.arbuzerxxl.vibeshot.features.details.navigation.ParentDestination
import com.arbuzerxxl.vibeshot.features.details.navigation.navigateToDetails
import com.arbuzerxxl.vibeshot.features.interests.navigation.InterestsDestination
import com.arbuzerxxl.vibeshot.features.start.navigation.StartNavigator

fun NavHostController.startNavigator(shouldSkipAuth: Boolean): StartNavigator = object : StartNavigator {
    override fun onNavigateAfterStart() {
        popBackStack()
        if (shouldSkipAuth) navigateToBottomMenu() else navigateToAuth()
    }
}

fun NavHostController.authNavigator(): AuthNavigator = object : AuthNavigator {
    override fun onNavigateAfterAuth() {
        popBackStack()
        navigateToBottomMenu()
    }
}

fun NavHostController.bottomMenuNavigator(): BottomMenuNavigator = object : BottomMenuNavigator {

    override fun onLogOut() {
        popBackStack()
        navigateToAuth()
    }

    override fun onNavigateUp() {
        popBackStack()
    }

    override fun onNavigateToDetails(initialPhotoPosition: Int, parentDestination: String) {

        val parentRoute = when (parentDestination) {
            InterestsDestination::class.java.name -> ParentDestination.Interests
            else -> throw IllegalArgumentException("Current parent destination doesn't exist")
        }

        navigateToDetails(initialPhotoPosition, parentRoute)
    }
}