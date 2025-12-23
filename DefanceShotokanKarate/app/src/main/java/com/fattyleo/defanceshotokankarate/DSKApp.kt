package com.fattyleo.defanceshotokankarate

import android.app.Application
import com.fattyleo.defanceshotokankarate.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DSKApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin
        startKoin {
            androidContext(this@DSKApp)
            androidLogger()

            modules(appModule)
        }
    }
}