package com.arbuzerxxl.vibeshot.navigation

import androidx.navigation.NavHostController
import com.arbuzerxxl.vibeshot.featires.details.navigation.navigateToDetails
import com.arbuzerxxl.vibeshot.features.auth.navigation.AuthNavigator
import com.arbuzerxxl.vibeshot.features.auth.navigation.navigateToAuth
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.BottomMenuNavigator
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.navigateToBottomMenu
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

    override fun onNavigateToDetails(url: String) {
        navigateToDetails(url=url)
    }
}