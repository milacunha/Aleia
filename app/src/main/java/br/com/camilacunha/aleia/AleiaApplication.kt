package br.com.camilacunha.aleia

import android.app.Application
import br.com.camilacunha.aleia.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AleiaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AleiaApplication)
            modules(appModule)
        }
    }
}