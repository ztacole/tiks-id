package com.zetta.tiksid

import android.app.Application
import com.zetta.tiksid.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TiksIDApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TiksIDApplication)
            modules(appModule)
        }
    }
}