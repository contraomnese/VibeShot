package com.arbuzerxxl.vibeshot.features.profile.di

import com.arbuzerxxl.vibeshot.features.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val profileModule = module {

    viewModelOf(::ProfileViewModel)
}