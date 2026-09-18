package br.com.camilacunha.aleia

import android.app.Application
import br.com.camilacunha.aleia.di.appModule
import br.com.camilacunha.aleia.di.networkModule
import br.com.camilacunha.aleia.di.repositoryModule
import br.com.camilacunha.aleia.domain.SeedBooksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.qualifier.named

class AleiaApplication : Application() {

    private val appScope: CoroutineScope by inject(named(APP_SCOPE))
    private val seedBooksRepository: SeedBooksRepository by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AleiaApplication)
            modules(
                appModule,
                networkModule,
                repositoryModule
            )
        }

        appScope.launch {
            seedBooksRepository.seedIfNeeded()
        }
    }

    companion object {
        const val APP_SCOPE = "appScope"
    }
}