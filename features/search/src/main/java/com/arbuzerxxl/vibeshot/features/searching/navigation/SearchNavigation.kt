package com.arbuzerxxl.vibeshot.features.searching.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.navigation.TopLevelDestination
import com.arbuzerxxl.vibeshot.features.searching.di.searchModule
import com.arbuzerxxl.vibeshot.features.searching.presentation.SearchRoute
import com.arbuzerxxl.vibeshot.ui.R
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object SearchDestination

data class SearchTopLevelDestination(
    override val icon: ImageVector = VibeShotIcons.Search,
    override val titleId: Int = R.string.search,
    override val route: Any = SearchDestination,
    override val destinationRoute: String = SearchDestination::class.java.name

): TopLevelDestination

interface SearchNavigator{
    fun navigateToDetails(initialPhotoPosition: Int, parentDestination: String)
    fun onNavigateUp()
}

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    navigate(SearchDestination, navOptions)
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.search(externalNavigator: SearchNavigator) {

    composable<SearchDestination> { navBack ->

        rememberKoinModules(unloadOnForgotten = true) { listOf(searchModule) }

        SearchRoute(onPhotoClicked = externalNavigator::navigateToDetails)
    }
}