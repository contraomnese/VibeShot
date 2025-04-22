/*
    Copyright (c) 2024. Kiparo.ru
 */

package com.kiparo.chargerapp.di

import com.arbuzerxxl.vibeshot.domain.usecases.auth.ObserveAuthStateUseCase
import com.arbuzerxxl.vibeshot.domain.usecases.photos.GetInterestsPhotosUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val domainModule = module {

    factory<ObserveAuthStateUseCase> {
        ObserveAuthStateUseCase(
            repository = get()
        )
    }
    factory<GetInterestsPhotosUseCase> {
        GetInterestsPhotosUseCase(
            interestsRepository = get(), photoSizesRepository = get(), dispatcher = Dispatchers.IO
        )
    }
}