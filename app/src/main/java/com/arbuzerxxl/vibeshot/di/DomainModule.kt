/*
    Copyright (c) 2024. Kiparo.ru
 */

package com.kiparo.chargerapp.di

import com.arbuzerxxl.vibeshot.domain.usecases.auth.ObserveAuthStateUseCase
import com.arbuzerxxl.vibeshot.domain.usecases.photo_tasks.GetPhotoTasksUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<ObserveAuthStateUseCase> {
        ObserveAuthStateUseCase(
            repository = get()
        )
    }
    factory<GetPhotoTasksUseCase> {
        GetPhotoTasksUseCase(
            repository = get()
        )
    }
}
