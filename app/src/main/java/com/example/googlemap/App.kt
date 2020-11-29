package com.example.googlemap

import android.app.Application
import com.example.googlemap.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(
                apiModule,
                networkModule,
                repoModule,
                informationRepo,
                locationRepo,
                viewModelModule)
            )
        }
    }
}
