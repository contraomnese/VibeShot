package com.arbuzerxxl.vibeshot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.arbuzerxxl.vibeshot.MainActivityViewModel
import com.arbuzerxxl.vibeshot.features.auth.navigation.authentication
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.bottomMenu
import com.arbuzerxxl.vibeshot.features.start.navigation.StartDestination
import com.arbuzerxxl.vibeshot.features.start.navigation.start
import org.koin.androidx.compose.koinViewModel

@Composable
fun VibeShotHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: MainActivityViewModel = koinViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val shouldSkipAuth = uiState.shouldSkipAuth()

    NavHost(
        navController,
        startDestination = StartDestination,
        modifier = modifier,
    ) {
        start(navController.startNavigator(shouldSkipAuth))
        authentication(navController.authNavigator())
        bottomMenu(navController.bottomMenuNavigator())
    }
}


