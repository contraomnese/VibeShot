/*
    Copyright (c) 2024. Kiparo.ru
 */

package com.kiparo.chargerapp.di

import com.arbuzerxxl.vibeshot.domain.usecases.auth.GetAccessTokenUseCase
import com.arbuzerxxl.vibeshot.domain.usecases.auth.GetAuthorizeUrlUseCase
import com.arbuzerxxl.vibeshot.domain.usecases.auth.GetRequestTokenUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val domainModule = module {

    factory<GetRequestTokenUseCase> {
        GetRequestTokenUseCase(
            dispatcher = Dispatchers.IO,
            authRepository = get()
        )
    }

    factory<GetAuthorizeUrlUseCase> {
        GetAuthorizeUrlUseCase(
            authRepository = get()
        )
    }

    factory<GetAccessTokenUseCase> {
        GetAccessTokenUseCase(
            dispatcher = Dispatchers.IO,
            authRepository = get()
        )
    }
}