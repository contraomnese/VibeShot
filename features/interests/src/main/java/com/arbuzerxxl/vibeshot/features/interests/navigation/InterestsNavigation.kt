package com.arbuzerxxl.vibeshot.features.interests.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.navigation.TopLevelDestination
import com.arbuzerxxl.vibeshot.features.interests.di.interestsModule
import com.arbuzerxxl.vibeshot.features.interests.presentation.InterestsRoute
import com.arbuzerxxl.vibeshot.ui.R
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object InterestsDestination

data class InterestsTopLevelDestination(
    override val icon: ImageVector = VibeShotIcons.Interests,
    override val titleId: Int = R.string.interests,
    override val route: Any = InterestsDestination,
    override val destinationRoute: String = InterestsDestination::class.java.name

): TopLevelDestination

interface InterestNavigator{
    fun navigateToDetails(photoId: String)
    fun onNavigateUp()
}

fun NavController.navigateToInterests(navOptions: NavOptions? = null) {
    navigate(InterestsDestination, navOptions)
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.interests(externalNavigator: InterestNavigator) {

    composable<InterestsDestination> {
        rememberKoinModules(unloadOnForgotten = true) { listOf(interestsModule) }

        InterestsRoute(onPhotoClicked = externalNavigator::navigateToDetails)
    }
}