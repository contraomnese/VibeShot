/*
    Copyright (c) 2024. Kiparo.ru
 */

package com.kiparo.chargerapp.di


import com.arbuzerxxl.vibeshot.BuildConfig
import com.arbuzerxxl.vibeshot.MainActivityViewModel
import com.arbuzerxxl.vibeshot.data.adapters.PhotoExifResponseTypeAdapter
import com.arbuzerxxl.vibeshot.data.adapters.PhotoInfoResponseTypeAdapter
import com.arbuzerxxl.vibeshot.data.adapters.PhotoSizesResponseTypeAdapter
import com.arbuzerxxl.vibeshot.data.mappers.AuthDataMapper
import com.arbuzerxxl.vibeshot.data.mediators.InterestsRemoteMediatorImpl
import com.arbuzerxxl.vibeshot.data.mediators.api.InterestsRemoteMediator
import com.arbuzerxxl.vibeshot.data.network.api.FlickrAuthApi
import com.arbuzerxxl.vibeshot.data.network.api.FlickrInterestsApi
import com.arbuzerxxl.vibeshot.data.network.api.FlickrPhotoApi
import com.arbuzerxxl.vibeshot.data.network.interceptors.ErrorInterceptor
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoExifResponse
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoInfoResponse
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizesResponse
import com.arbuzerxxl.vibeshot.data.repository.AuthRepositoryImpl
import com.arbuzerxxl.vibeshot.data.repository.InterestsRepositoryImpl
import com.arbuzerxxl.vibeshot.data.repository.PhotosRepositoryImpl
import com.arbuzerxxl.vibeshot.data.repository.TokenRepositoryImpl
import com.arbuzerxxl.vibeshot.data.repository.UserDataRepositoryImpl
import com.arbuzerxxl.vibeshot.data.repository.UserRepositoryImpl
import com.arbuzerxxl.vibeshot.data.storage.datastore.api.SettingsStorage
import com.arbuzerxxl.vibeshot.data.storage.datastore.api.UserStorage
import com.arbuzerxxl.vibeshot.data.storage.datastore.memory.SettingsMemoryStorage
import com.arbuzerxxl.vibeshot.data.storage.datastore.memory.UserMemoryStorage
import com.arbuzerxxl.vibeshot.data.storage.db.AppDatabase
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import com.arbuzerxxl.vibeshot.domain.repository.TokenRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserDataRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val dataModule = module {

    single<Gson> {
        GsonBuilder()
            .registerTypeAdapter(PhotoExifResponse::class.java, PhotoExifResponseTypeAdapter())
            .registerTypeAdapter(PhotoInfoResponse::class.java, PhotoInfoResponseTypeAdapter())
            .registerTypeAdapter(PhotoSizesResponse::class.java, PhotoSizesResponseTypeAdapter())
            .create()
    }

    // region Network
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.FLICKR_API_BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get()))
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

    single<AppDatabase> { AppDatabase.create(context = get()) }

    single<FlickrAuthApi> { get<Retrofit>().create(FlickrAuthApi::class.java) }
    single<FlickrInterestsApi> { get<Retrofit>().create(FlickrInterestsApi::class.java) }
    single<FlickrPhotoApi> { get<Retrofit>().create(FlickrPhotoApi::class.java) }
    // endregion

    // region Repositories
    single<TokenRepository> {
        TokenRepositoryImpl(
            api = get(),
            apiKey = BuildConfig.FLICKR_API_KEY,
            apiToken = BuildConfig.FLICKR_SECRET,
            apiBaseUrl = BuildConfig.FLICKR_API_BASE_URL,
            apiCallback = BuildConfig.FLICKR_API_CALLBACK,
            dispatcher = Dispatchers.IO
        )
    }
    factory<UserRepository> {
        UserRepositoryImpl(
            userStorage = get(),
            mapper = get(),
            tokenRepository = get(),
            dispatcher = Dispatchers.IO
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
            settingsStorage = get(),
            dispatcher = Dispatchers.IO
        )
    }
    factory<InterestsRepository> {
        InterestsRepositoryImpl(
            database = get(),
            dispatcher = Dispatchers.IO
        )
    }
    factory<PhotosRepository> {
        PhotosRepositoryImpl(
            api = get(),
            key = BuildConfig.FLICKR_API_KEY,
            dispatcher = Dispatchers.IO,
            database = get()
        )
    }
    // endregion

    // region mediators
    single<InterestsRemoteMediator> {
        InterestsRemoteMediatorImpl(
            database = get(),
            api = get(),
            key = BuildConfig.FLICKR_API_KEY,
            photosRepository = get(),
            dispatcher = Dispatchers.IO
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

    // region viewModels
    single<MainActivityViewModel> {
        MainActivityViewModel(userDataRepository = get())
    }
    // endregion
}