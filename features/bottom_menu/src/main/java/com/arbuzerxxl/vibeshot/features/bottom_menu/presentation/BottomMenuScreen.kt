package com.arbuzerxxl.vibeshot.features.bottom_menu.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arbuzerxxl.vibeshot.core.design.theme.padding80
import com.arbuzerxxl.vibeshot.core.navigation.TopLevelDestination
import com.arbuzerxxl.vibeshot.core.navigation.navigateSingleTopTo
import com.arbuzerxxl.vibeshot.core.ui.utils.NetworkStatus
import com.arbuzerxxl.vibeshot.core.ui.widgets.ErrorSnackBar
import com.arbuzerxxl.vibeshot.core.ui.widgets.NavBar
import com.arbuzerxxl.vibeshot.core.ui.widgets.NetworkDisconnectionBanner
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.BottomMenuNavigator
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.interestsNavigator
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.searchNavigator
import com.arbuzerxxl.vibeshot.features.interests.navigation.InterestsDestination
import com.arbuzerxxl.vibeshot.features.interests.navigation.interests
import com.arbuzerxxl.vibeshot.features.profile.navigation.profile
import com.arbuzerxxl.vibeshot.features.searching.navigation.SearchDestination
import com.arbuzerxxl.vibeshot.features.searching.navigation.search
import com.arbuzerxxl.vibeshot.features.tasks.navigation.tasks
import com.kiparo.pizzaapp.presentation.features.bottom_menu.BottomMenuViewModel
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun BottomMenuRoute(
    modifier: Modifier = Modifier,
    viewmodel: BottomMenuViewModel = koinViewModel(),
    externalNavigator: BottomMenuNavigator,
    initialSearchTag: String?
) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewmodel.errorEvents.collect { message ->
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    BottomMenuScreen(
        topLevelDestinations = uiState.topLevelDestinations,
        externalNavigator = externalNavigator,
        snackBarHostState = snackBarHostState,
        networkStatus = uiState.networkStatus,
        initialSearchTag = initialSearchTag
    )

}

@Composable
internal fun BottomMenuScreen(
    networkStatus: NetworkStatus,
    externalNavigator: BottomMenuNavigator,
    snackBarHostState: SnackbarHostState,
    topLevelDestinations: ImmutableList<TopLevelDestination>,
    initialSearchTag: String?
) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val startDestination = initialSearchTag?.let { SearchDestination(initialSearchTag) } ?: InterestsDestination

    Scaffold(
        bottomBar = {
            NavBar(
                currentDestination = currentRoute,
                destinations = topLevelDestinations,
                onNavigateToTopLevel = { route ->
                    navController.navigateSingleTopTo(route)
                },
            )
        },
        snackbarHost = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = padding80)
            ) {
                SnackbarHost(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(1f),
                    hostState = snackBarHostState,
                    snackbar = { snackBarData ->
                        ErrorSnackBar(message = snackBarData.visuals.message)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = startDestination) {
                interests(navController.interestsNavigator(externalNavigator))
                search(navController.searchNavigator(externalNavigator))
                tasks()
                profile(externalNavigator::onLogOut)
            }
            if (networkStatus == NetworkStatus.Disconnected)
                NetworkDisconnectionBanner()
        }
    }
}