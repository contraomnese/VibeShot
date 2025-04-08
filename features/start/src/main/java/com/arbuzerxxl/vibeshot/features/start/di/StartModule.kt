package com.arbuzerxxl.vibeshot.features.start.di

import com.arbuzerxxl.vibeshot.features.start.StartViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val startModule = module {

    viewModelOf(::StartViewModel)
}