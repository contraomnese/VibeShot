package com.arbuzerxxl.vibeshot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.arbuzerxxl.vibeshot.MainActivityViewModel
import com.arbuzerxxl.vibeshot.features.auth.navigation.AuthDestination
import com.arbuzerxxl.vibeshot.features.auth.navigation.authentication
import com.arbuzerxxl.vibeshot.features.interests.navigation.InterestsDestination
import com.arbuzerxxl.vibeshot.features.interests.navigation.interests
import com.arbuzerxxl.vibeshot.features.start.navigation.StartDestination
import com.arbuzerxxl.vibeshot.features.start.navigation.navigateToStart
import com.arbuzerxxl.vibeshot.features.start.navigation.start
import org.koin.androidx.compose.koinViewModel

@Composable
fun VibeShotHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: MainActivityViewModel = koinViewModel(),
) {

    val uiState by viewModel.uiState.collectAsState()
    val startDestination = if (uiState.shouldSkipAuth()) InterestsDestination else AuthDestination

    NavHost(
        navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        start()
        authentication(onNavigateAfterAuth = navController::navigateToStart,)
        interests()
    }
}


