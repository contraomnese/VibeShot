package com.arbuzerxxl.vibeshot.featires.details.di

import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsPhoto
import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val detailsModule = module {
    
    viewModel { params ->
        val photo = params.get<DetailsPhoto>()
        DetailsViewModel(photo)
    }
}