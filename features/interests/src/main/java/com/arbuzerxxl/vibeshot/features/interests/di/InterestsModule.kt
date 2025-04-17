package com.arbuzerxxl.vibeshot.features.interests.di

import com.arbuzerxxl.vibeshot.features.interests.presentation.InterestsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val interestsModule = module {

    viewModelOf(::InterestsViewModel)
}