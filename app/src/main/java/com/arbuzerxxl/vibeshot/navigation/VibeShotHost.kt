package com.arbuzerxxl.vibeshot.navigation

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.arbuzerxxl.vibeshot.features.auth.navigation.AuthRoute
import com.arbuzerxxl.vibeshot.features.auth.navigation.authentication
import com.arbuzerxxl.vibeshot.features.start.navigation.StartRoute
import com.arbuzerxxl.vibeshot.features.start.navigation.navigateToStart
import com.arbuzerxxl.vibeshot.features.start.navigation.start
import kotlinx.serialization.Serializable

@Composable
fun VibeShotHost(
    navHostController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {

    NavHost(
        navHostController,
        startDestination = StartRoute,
        modifier = modifier,
    ) {
        start(navigateToAuth = {
            navHostController.navigate(AuthRoute) {
                popUpTo(StartRoute) { inclusive = true }
            }
        })
        authentication(
            onNavigateToUserScreen = navHostController::navigateToExampleUserScreen,
            onNavigateToGuestScreen = navHostController::navigateToExampleGuestScreen
        )
        exampleUser()
        exampleGuest()
    }
}

// example user screen

@Serializable
data object ExampleUserRoute

fun NavController.navigateToExampleUserScreen() {
    popBackStack()
    navigate(ExampleUserRoute)
}

fun NavGraphBuilder.exampleUser() {
    composable<ExampleUserRoute> {
        ExampleUserScreen()
    }

}

@Composable
internal fun ExampleUserScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "User Screen")
        }
    }
}

// example guest screen

@Serializable
data object ExampleGuestRoute

fun NavController.navigateToExampleGuestScreen() {
    popBackStack()
    navigate(ExampleGuestRoute)
}

fun NavGraphBuilder.exampleGuest() {
    composable<ExampleGuestRoute> {
        ExampleGuestScreen()
    }

}

@Composable
internal fun ExampleGuestScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "Guest Screen")
        }
    }
}