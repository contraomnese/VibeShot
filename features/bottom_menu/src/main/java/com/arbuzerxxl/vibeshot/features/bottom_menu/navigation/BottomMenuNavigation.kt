package com.arbuzerxxl.vibeshot.features.bottom_menu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.arbuzerxxl.vibeshot.core.navigation.navigateSingleTopTo
import com.arbuzerxxl.vibeshot.features.bottom_menu.di.bottomMenuModule
import com.kiparo.pizzaapp.presentation.features.bottom_menu.BottomMenuRoute
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
}

fun NavGraphBuilder.bottomMenu(
    externalNavigator: BottomMenuNavigator
) {
    navigation<BottomMenuGraph>(startDestination = BottomMenuDestination) {
        bottomMenuInner(
            onLogOutClicked = externalNavigator::onLogOut
        )
    }
}

@OptIn(KoinExperimentalAPI::class)
private fun NavGraphBuilder.bottomMenuInner(
    onLogOutClicked: () -> Unit
) {
    composable<BottomMenuDestination> {

        rememberKoinModules(unloadOnForgotten = true) { listOf(bottomMenuModule) }

        BottomMenuRoute(onLogOutClicked = onLogOutClicked)

    }
}

fun NavHostController.navigateToBottomMenu() {
    popBackStack()
    navigateSingleTopTo(BottomMenuGraph)
}