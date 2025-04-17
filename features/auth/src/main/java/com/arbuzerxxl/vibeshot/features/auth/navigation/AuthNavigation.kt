package com.arbuzerxxl.vibeshot.features.auth.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arbuzerxxl.vibeshot.features.auth.di.authModule
import com.arbuzerxxl.vibeshot.features.auth.presentation.AuthRoute
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object AuthDestination

fun NavController.navigateToAuth(navOptions: NavOptions? = null) =
    navigate(AuthDestination, navOptions)

interface AuthNavigator {
    fun onNavigateAfterAuth()
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.authentication(
    externalNavigator: AuthNavigator
) {
    composable<AuthDestination> {

        rememberKoinModules(unloadOnForgotten = true) { listOf(authModule) }

        AuthRoute(
            onNavigateAfterAuth = externalNavigator::onNavigateAfterAuth,
        )
    }

}