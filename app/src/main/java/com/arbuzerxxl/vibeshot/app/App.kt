package com.arbuzerxxl.vibeshot.app

import android.app.Application
import com.kiparo.chargerapp.di.dataModule
import com.kiparo.chargerapp.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                dataModule,
                domainModule
            )
        }
    }
}