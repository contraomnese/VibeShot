package com.arbuzerxxl.vibeshot.app

import android.app.Application
import android.os.StrictMode
import com.arbuzerxxl.vibeshot.BuildConfig
import com.arbuzerxxl.vibeshot.di.featuresApiModule
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
                domainModule,
                featuresApiModule
            )
        }
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
        }
    }
}