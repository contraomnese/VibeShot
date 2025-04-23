package com.arbuzerxxl.vibeshot.featires.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.arbuzerxxl.vibeshot.featires.details.di.detailsModule
import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsPhoto
import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsRoute
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
data class DetailsDestination(val url: String)

fun NavController.navigateToDetails(url: String, navOptions: NavOptions? = null) {
    navigate(DetailsDestination(url), navOptions)
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.details(onNavigateUp: () -> Unit) {

    composable<DetailsDestination> { navBackStackEntry ->

        val detailsDestination = navBackStackEntry.toRoute<DetailsDestination>()
        val photo = DetailsPhoto(
            url = detailsDestination.url,
        )

        rememberKoinModules(unloadOnForgotten = true) { listOf(detailsModule) }

        DetailsRoute(photo)
    }

}