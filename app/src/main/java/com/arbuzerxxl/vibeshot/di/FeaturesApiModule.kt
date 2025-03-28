/*
    Copyright (c) 2024. Kiparo.ru
 */

package com.arbuzerxxl.vibeshot.di

import com.arbuzerxxl.vibeshot.features.auth_impl.presentation.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featuresApiModule = module {
    viewModel<AuthViewModel> { AuthViewModel(get(), get(), get()) }
}