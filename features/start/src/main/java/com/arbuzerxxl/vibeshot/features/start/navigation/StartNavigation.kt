package com.arbuzerxxl.vibeshot.features.start.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arbuzerxxl.vibeshot.features.start.StartRoute
import com.arbuzerxxl.vibeshot.features.start.di.startModule
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object StartDestination

fun NavController.navigateToStart(navOptions: NavOptions? = null) {
    navigate(StartDestination, navOptions)
}

interface StartNavigator {
    fun onNavigateAfterStart()
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.start(
    externalNavigator: StartNavigator
) {
    composable<StartDestination> {
        rememberKoinModules(unloadOnForgotten = true) { listOf(startModule) }

        StartRoute(onNavigateAfterStart = externalNavigator::onNavigateAfterStart)
    }
}