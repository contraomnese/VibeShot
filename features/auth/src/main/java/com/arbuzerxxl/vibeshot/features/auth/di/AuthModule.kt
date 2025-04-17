package com.arbuzerxxl.vibeshot.features.auth.di

import com.arbuzerxxl.vibeshot.features.auth.presentation.AuthViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val authModule = module {

    viewModelOf(::AuthViewModel)
}