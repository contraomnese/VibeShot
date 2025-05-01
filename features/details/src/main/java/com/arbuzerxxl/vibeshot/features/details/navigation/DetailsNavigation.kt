package com.arbuzerxxl.vibeshot.features.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.arbuzerxxl.vibeshot.features.details.di.detailsModule
import com.arbuzerxxl.vibeshot.features.details.presentation.DetailsRoute
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI


@Serializable
data class DetailsDestination(val initialPhotoPosition: Int, val parentDestination: String)

fun NavController.navigateToDetails(initialPhotoPosition: Int, parentDestination: String, navOptions: NavOptions? = null) {
    navigate(DetailsDestination(initialPhotoPosition, parentDestination), navOptions)
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.details() {

    composable<DetailsDestination> { navBackStackEntry ->

        val detailsDestination = navBackStackEntry.toRoute<DetailsDestination>()

        val photoPosition = detailsDestination.initialPhotoPosition
        val parentDestination = detailsDestination.parentDestination

        rememberKoinModules(unloadOnForgotten = true) { listOf(detailsModule) }
        DetailsRoute(photoPosition = photoPosition, parentDestination = parentDestination)
    }

}