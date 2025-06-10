package com.arbuzerxxl.vibeshot.features.bottom_menu.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
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
data class BottomMenuDestination(val initialSearchTag: String? = null)

interface BottomMenuNavigator {
    fun onLogOut()
    fun onNavigateUp()
    fun onNavigateToDetails(initialPhotoPosition: Int, parentDestination: String)
    fun onNavigateToSearchByTag(tag: String)
}

fun NavGraphBuilder.bottomMenu(
    externalNavigator: BottomMenuNavigator,
) {

    navigation<BottomMenuGraph>(startDestination = BottomMenuDestination()) {
        bottomMenuInner(externalNavigator = externalNavigator)
        details(
            onNavigateUp = externalNavigator::onNavigateUp,
            onNavigateToSearchByTag = externalNavigator::onNavigateToSearchByTag
        )
    }
}

@OptIn(KoinExperimentalAPI::class)
private fun NavGraphBuilder.bottomMenuInner(
    externalNavigator: BottomMenuNavigator,
) {

    composable<BottomMenuDestination> { navBackStackEntry ->

        rememberKoinModules(unloadOnForgotten = true) { listOf(bottomMenuModule) }

        val args = remember { navBackStackEntry.toRoute<BottomMenuDestination>()}

        BottomMenuRoute(
            externalNavigator = externalNavigator,
            initialSearchTag = args.initialSearchTag
        )

    }
}

fun NavHostController.navigateToBottomMenu() {
    popBackStack()
    navigateSingleTopTo(BottomMenuGraph)
}