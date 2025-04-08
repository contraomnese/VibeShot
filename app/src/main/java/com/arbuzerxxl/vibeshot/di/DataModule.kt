/*
    Copyright (c) 2024. Kiparo.ru
 */

package com.kiparo.chargerapp.di


import com.arbuzerxxl.vibeshot.BuildConfig
import com.arbuzerxxl.vibeshot.MainActivityViewModel
import com.arbuzerxxl.vibeshot.data.mappers.AuthDataMapper
import com.arbuzerxxl.vibeshot.data.network.api.FlickrAuthApi
import com.arbuzerxxl.vibeshot.data.network.interceptors.ErrorInterceptor
import com.arbuzerxxl.vibeshot.data.repository.AuthRepositoryImpl
import com.arbuzerxxl.vibeshot.data.repository.TokenRepositoryImpl
import com.arbuzerxxl.vibeshot.data.repository.UserDataRepositoryImpl
import com.arbuzerxxl.vibeshot.data.repository.UserRepositoryImpl
import com.arbuzerxxl.vibeshot.data.storage.api.SettingsStorage
import com.arbuzerxxl.vibeshot.data.storage.api.UserStorage
import com.arbuzerxxl.vibeshot.data.storage.memory.SettingsMemoryStorage
import com.arbuzerxxl.vibeshot.data.storage.memory.UserMemoryStorage
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.domain.repository.TokenRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserDataRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserRepository
import com.arbuzerxxl.vibeshot.features.auth.presentation.AuthViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val dataModule = module {

    // region Network
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.FLICKR_API_BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<ErrorInterceptor>())
            .build()
    }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }
    }

    factory<ErrorInterceptor> {
        ErrorInterceptor()
    }

    single<FlickrAuthApi> { get<Retrofit>().create(FlickrAuthApi::class.java) }
    // endregion

    // region Repositories
    single<TokenRepository> {
        TokenRepositoryImpl(
            api = get(),
            apiKey = BuildConfig.FLICKR_API_KEY,
            apiToken = BuildConfig.FLICKR_SECRET,
            apiBaseUrl = BuildConfig.FLICKR_API_BASE_URL,
            apiCallback = BuildConfig.FLICKR_API_CALLBACK
        )
    }
    factory<UserRepository> {
        UserRepositoryImpl(
            userStorage = get(),
            mapper = get(),
            tokenRepository = get()
        )
    }
    factory<AuthRepository> {
        AuthRepositoryImpl(
            userRepository = get(),
            tokenRepository = get(),
            dispatcher = Dispatchers.IO
        )
    }
    factory<UserDataRepository> {
        UserDataRepositoryImpl(
            authRepository = get(),
            settingsStorage = get()
        )
    }
    // endregion

    // region Storages
    single<UserStorage> {
        UserMemoryStorage(
            context = get()
        )
    }
    single<SettingsStorage> {
        SettingsMemoryStorage(
            context = get()
        )
    }
    // endregion

    // region Mappers
    single<AuthDataMapper> {
        AuthDataMapper()
    }
    // endregion
    single<AuthViewModel> {
        AuthViewModel(authRepository = get(), observeAuthStateUseCase = get())
    }
    single<MainActivityViewModel> {
        MainActivityViewModel(userDataRepository = get())
    }
}