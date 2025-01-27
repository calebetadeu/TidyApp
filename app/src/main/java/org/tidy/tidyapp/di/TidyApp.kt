package org.tidy.tidyapp.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.tidy.feature_auth.di.authModule
import org.tidy.feature_clients.di.clientModule

class TidyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TidyApp)
            modules(authModule, clientModule) // Registra o módulo de autenticação
        }
    }
}