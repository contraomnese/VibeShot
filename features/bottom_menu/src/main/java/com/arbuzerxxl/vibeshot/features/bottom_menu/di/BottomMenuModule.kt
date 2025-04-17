package com.arbuzerxxl.vibeshot.features.bottom_menu.di

import com.arbuzerxxl.vibeshot.core.navigation.TopDestinationsCollection
import com.arbuzerxxl.vibeshot.features.interests.navigation.InterestsTopLevelDestination
import com.arbuzerxxl.vibeshot.features.profile.navigation.ProfileTopLevelDestination
import com.kiparo.pizzaapp.presentation.features.bottom_menu.BottomMenuViewModel
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val bottomMenuModule = module {

    viewModelOf(::BottomMenuViewModel)

    // navigation
    single<TopDestinationsCollection> { TopDestinationsCollection(
        items = persistentListOf(
            InterestsTopLevelDestination(),
            ProfileTopLevelDestination()
        )
    ) }
    // endregion
}