package com.arbuzerxxl.vibeshot.features.details.di

import com.arbuzerxxl.vibeshot.features.details.navigation.ParentDestination
import com.arbuzerxxl.vibeshot.features.details.presentation.DetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailsModule = module {

    viewModel { params ->
        val destination = params.get<ParentDestination>()
        DetailsViewModel(
            interestsRepository = get(),
            photosRepository = get(),
            searchRepository = get(),
            parentDestination = destination,
            networkMonitor = get()
        )
    }
}