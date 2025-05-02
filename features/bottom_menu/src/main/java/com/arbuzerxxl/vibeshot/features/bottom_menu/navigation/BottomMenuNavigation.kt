package com.arbuzerxxl.vibeshot.features.bottom_menu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.arbuzerxxl.vibeshot.core.navigation.navigateSingleTopTo
import com.arbuzerxxl.vibeshot.features.bottom_menu.di.bottomMenuModule
import com.arbuzerxxl.vibeshot.features.bottom_menu.presentation.BottomMenuRoute
import com.arbuzerxxl.vibeshot.features.details.navigation.details
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object BottomMenuGraph

@Serializable
object BottomMenuDestination

interface BottomMenuNavigator {
    fun onLogOut()
    fun onNavigateUp()
    fun onNavigateToDetails(initialPhotoPosition: Int, parentDestination: String)
}

fun NavGraphBuilder.bottomMenu(
    externalNavigator: BottomMenuNavigator
) {

    navigation<BottomMenuGraph>(startDestination = BottomMenuDestination) {
        bottomMenuInner(externalNavigator)
        details(externalNavigator::onNavigateUp)
    }
}

@OptIn(KoinExperimentalAPI::class)
private fun NavGraphBuilder.bottomMenuInner(
    externalNavigator: BottomMenuNavigator
) {

    composable<BottomMenuDestination> {

        rememberKoinModules(unloadOnForgotten = true) { listOf(bottomMenuModule) }

        BottomMenuRoute(externalNavigator = externalNavigator)

    }
}

fun NavHostController.navigateToBottomMenu() {
    popBackStack()
    navigateSingleTopTo(BottomMenuGraph)
}