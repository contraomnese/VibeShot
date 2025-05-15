package com.arbuzerxxl.vibeshot.features.tasks.di

import com.arbuzerxxl.vibeshot.features.tasks.presentation.TasksViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val tasksModule = module {

    viewModelOf(::TasksViewModel)
}