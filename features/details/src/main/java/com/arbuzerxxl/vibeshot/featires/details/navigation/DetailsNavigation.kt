package com.arbuzerxxl.vibeshot.featires.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.arbuzerxxl.vibeshot.featires.details.di.detailsModule
import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsPhotoIndex
import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsRoute
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
data class DetailsDestination(val initialPhotoIndex: Int)

fun NavController.navigateToDetails(initialIndex: Int, navOptions: NavOptions? = null) {
    navigate(DetailsDestination(initialIndex), navOptions)
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.details(onNavigateUp: () -> Unit) {

    composable<DetailsDestination> { navBackStackEntry ->

        val detailsDestination = navBackStackEntry.toRoute<DetailsDestination>()
        val index = DetailsPhotoIndex(index = detailsDestination.initialPhotoIndex)

        rememberKoinModules(unloadOnForgotten = true) { listOf(detailsModule) }

        DetailsRoute(initialPhotoIndex = index)
    }

}