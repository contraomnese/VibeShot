package com.arbuzerxxl.vibeshot.features.searching.di

import com.arbuzerxxl.vibeshot.features.searching.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val searchModule = module {
    viewModelOf(::SearchViewModel)
}