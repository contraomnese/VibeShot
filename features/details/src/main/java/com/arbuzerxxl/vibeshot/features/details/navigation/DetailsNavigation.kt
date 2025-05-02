package com.arbuzerxxl.vibeshot.features.details.navigation

import android.os.Bundle
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.arbuzerxxl.vibeshot.features.details.di.detailsModule
import com.arbuzerxxl.vibeshot.features.details.presentation.DetailsRoute
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.reflect.typeOf


@Serializable
data class DetailsDestination(val initialPhotoPosition: Int, val parentDestination: ParentDestination)

fun NavController.navigateToDetails(
    initialPhotoPosition: Int,
    parentDestination: ParentDestination,
    navOptions: NavOptions? = null,
) {
    navigate(DetailsDestination(initialPhotoPosition, parentDestination), navOptions)
}

@Serializable
@Immutable
sealed class ParentDestination {
    @Serializable
    @SerialName("Interests")
    data object Interests : ParentDestination()

    @Serializable
    @SerialName("Search")
    data object Search : ParentDestination()

    companion object {
        val navType = object : NavType<ParentDestination>(isNullableAllowed = false) {

            override fun put(bundle: Bundle, key: String, value: ParentDestination) {
                bundle.putString(key, value::class.simpleName)
            }

            override fun get(bundle: Bundle, key: String): ParentDestination? {
                return when (bundle.getString(key)) {
                    "Interests" -> ParentDestination.Interests
                    "Search" -> ParentDestination.Search
                    else -> throw IllegalArgumentException("Unknown ParentDestination type")
                }
            }

            override fun parseValue(value: String): ParentDestination {
                return when (value) {
                    "Interests" -> ParentDestination.Interests
                    "Search" -> ParentDestination.Search
                    else -> throw IllegalArgumentException("Unknown ParentDestination type: $value")
                }
            }
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.details(onNavigateUp: () -> Unit) {

    composable<DetailsDestination>(typeMap = mapOf(
        typeOf<ParentDestination>() to ParentDestination.navType
    )) { navBackStackEntry ->

        val args = remember {
            navBackStackEntry.toRoute<DetailsDestination>()
        }

        val photoPosition = args.initialPhotoPosition
        val parentDestination = args.parentDestination

        rememberKoinModules(unloadOnForgotten = true) { listOf(detailsModule) }

        DetailsRoute(
            photoPosition = photoPosition,
            parentDestination = parentDestination,
            onNavigateUp = onNavigateUp
        )
    }

}