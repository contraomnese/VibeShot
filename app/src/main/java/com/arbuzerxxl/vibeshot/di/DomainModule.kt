/*
    Copyright (c) 2024. Kiparo.ru
 */

package com.kiparo.chargerapp.di

import com.arbuzerxxl.vibeshot.domain.usecases.auth.ObserveAuthStateUseCase
import com.arbuzerxxl.vibeshot.domain.usecases.photos.GetInterestsPhotosUseCaseSuspend
import org.koin.dsl.module

val domainModule = module {

    factory<ObserveAuthStateUseCase> {
        ObserveAuthStateUseCase(
            repository = get()
        )
    }
    single<GetInterestsPhotosUseCaseSuspend> {
        GetInterestsPhotosUseCaseSuspend(
            repository = get()
        )
    }
}