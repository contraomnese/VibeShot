package com.arbuzerxxl.vibeshot.navigation

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import com.arbuzerxxl.vibeshot.ui.VibeShotAppState
import kotlinx.serialization.Serializable

@Composable
fun VibeShotHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    skipAuth: Boolean
) {

    NavHost(
        navController,
        startDestination = if (skipAuth) StartRoute else AuthRoute,
        modifier = modifier,
    ) {
        start()
        authentication(
            onNavigateAfterAuth = navController::navigateToStart,
        )
    }
}


