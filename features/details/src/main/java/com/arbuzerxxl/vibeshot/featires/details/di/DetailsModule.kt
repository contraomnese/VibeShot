package com.arbuzerxxl.vibeshot.featires.details.di

import com.arbuzerxxl.vibeshot.featires.details.presentation.DetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val detailsModule = module {
    
    viewModelOf(::DetailsViewModel)
}