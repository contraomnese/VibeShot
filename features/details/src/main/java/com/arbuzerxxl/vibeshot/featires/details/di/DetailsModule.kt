package com.arbuzerxxl.vibeshot.featires.details.di

import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsPhotoId
import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val detailsModule = module {
    
    viewModel { params ->
        val id = params.get<DetailsPhotoId>()
        DetailsViewModel(id = id, getInterestsPhotosUseCase = get())
    }
}