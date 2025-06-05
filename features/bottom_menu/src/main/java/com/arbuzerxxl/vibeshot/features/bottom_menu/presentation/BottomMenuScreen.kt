package com.arbuzerxxl.vibeshot.features.bottom_menu.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arbuzerxxl.vibeshot.core.design.theme.padding4
import com.arbuzerxxl.vibeshot.core.navigation.TopLevelDestination
import com.arbuzerxxl.vibeshot.core.navigation.navigateSingleTopTo
import com.arbuzerxxl.vibeshot.core.ui.widgets.ConnectionBanner
import com.arbuzerxxl.vibeshot.core.ui.widgets.NavBar
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.BottomMenuNavigator
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.interestsNavigator
import com.arbuzerxxl.vibeshot.features.bottom_menu.navigation.searchNavigator
import com.arbuzerxxl.vibeshot.features.interests.navigation.InterestsDestination
import com.arbuzerxxl.vibeshot.features.interests.navigation.interests
import com.arbuzerxxl.vibeshot.features.profile.navigation.profile
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
) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    BottomMenuScreen(
        topLevelDestinations = uiState.topLevelDestinations,
        externalNavigator = externalNavigator,
        isNetworkConnected = uiState.isNetworkConnected
    )

}

@Composable
internal fun BottomMenuScreen(
    isNetworkConnected: Boolean,
    externalNavigator: BottomMenuNavigator,
    topLevelDestinations: ImmutableList<TopLevelDestination>,
) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavBar(
                currentDestination = currentRoute,
                destinations = topLevelDestinations,
                onNavigateToTopLevel = { route ->
                    navController.navigateSingleTopTo(route)
                },
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = InterestsDestination) {
                interests(navController.interestsNavigator(externalNavigator))
                search(navController.searchNavigator(externalNavigator))
                tasks()
                profile(externalNavigator::onLogOut)
            }
            if (!isNetworkConnected)
                ConnectionBanner(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = padding4)
                )
        }
    }
}