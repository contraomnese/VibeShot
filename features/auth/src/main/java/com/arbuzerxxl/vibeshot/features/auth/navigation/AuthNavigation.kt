package com.arbuzerxxl.vibeshot.features.auth.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arbuzerxxl.vibeshot.features.auth.presentation.AuthRoute
import kotlinx.serialization.Serializable

@Serializable
object AuthDestination

fun NavController.navigateToAuth(navOptions: NavOptions? = null) =
    navigate(AuthDestination, navOptions)

fun NavGraphBuilder.authentication(
    onNavigateAfterAuth: () -> Unit,
) {
    composable<AuthDestination> {
        AuthRoute(
            onNavigateAfterAuth = onNavigateAfterAuth,
        )
    }

}