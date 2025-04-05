package com.arbuzerxxl.vibeshot.features.auth.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arbuzerxxl.vibeshot.features.auth.presentation.AuthRoute
import kotlinx.serialization.Serializable

@Serializable
data object AuthRoute

fun NavController.navigateToAuth(navOptions: NavOptions? = null) =
    navigate(AuthRoute, navOptions)

fun NavGraphBuilder.authentication(
    onNavigateToUserScreen: () -> Unit,
    onNavigateToGuestScreen: () -> Unit,
) {
    composable<AuthRoute> {
        AuthRoute(
            onNavigateToUser = onNavigateToUserScreen,
            onNavigateToGuest = onNavigateToGuestScreen
        )
    }

}