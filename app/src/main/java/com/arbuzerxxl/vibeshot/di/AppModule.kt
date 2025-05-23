package com.arbuzerxxl.vibeshot.di

import com.arbuzerxxl.vibeshot.BuildConfig
import com.arbuzerxxl.vibeshot.domain.models.app.ApplicationId
import org.koin.dsl.module

val appModule = module {

    single<ApplicationId> { ApplicationId(id = BuildConfig.APPLICATION_ID) }

}