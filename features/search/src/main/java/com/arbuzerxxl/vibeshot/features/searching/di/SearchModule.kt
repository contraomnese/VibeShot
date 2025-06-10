package com.arbuzerxxl.vibeshot.features.searching.di

import com.arbuzerxxl.vibeshot.features.searching.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val searchModule = module {

    viewModel { params ->
        val searchTag = params.getOrNull<String>()
        SearchViewModel(
            searchRepository =get(),
            errorMonitor = get(),
            searchTag = searchTag
        )
    }
}