package com.example.mymurmurapp

import android.app.Application
import com.example.mymurmurapp.di.AppContainer

class MurmurApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.init(this)
    }
}

