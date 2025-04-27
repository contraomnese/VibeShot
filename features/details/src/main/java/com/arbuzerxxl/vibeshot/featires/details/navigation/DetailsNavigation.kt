package com.arbuzerxxl.vibeshot.featires.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.arbuzerxxl.vibeshot.featires.details.di.detailsModule
import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsPhotoId
import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsRoute
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
data class DetailsDestination(val id: String)

fun NavController.navigateToDetails(photoId: String, navOptions: NavOptions? = null) {
    navigate(DetailsDestination(photoId), navOptions)
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.details(onNavigateUp: () -> Unit) {

    composable<DetailsDestination> { navBackStackEntry ->

        val detailsDestination = navBackStackEntry.toRoute<DetailsDestination>()
        val id = DetailsPhotoId(id = detailsDestination.id)


        rememberKoinModules(unloadOnForgotten = true) { listOf(detailsModule) }

        DetailsRoute(id)
    }

}