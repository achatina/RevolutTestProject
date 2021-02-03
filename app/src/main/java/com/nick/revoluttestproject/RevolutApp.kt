package com.nick.revoluttestproject

import android.app.Application
import com.nick.revoluttestproject.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RevolutApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RevolutApp)
            modules(mainModule)
        }
    }

}