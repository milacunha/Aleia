package br.com.camilacunha.aleia.di

import br.com.camilacunha.aleia.AleiaApplication.Companion.APP_SCOPE
import br.com.camilacunha.aleia.data.local.seed.SeedBookDataSource
import br.com.camilacunha.aleia.data.local.seed.SeedPreferences
import br.com.camilacunha.aleia.data.repository.BookRemoteRepositoryImpl
import br.com.camilacunha.aleia.data.repository.SaveBooksRepositoryImpl
import br.com.camilacunha.aleia.data.repository.SeedBooksRepositoryImpl
import br.com.camilacunha.aleia.domain.BookRemoteRepository
import br.com.camilacunha.aleia.domain.SaveBooksRepository
import br.com.camilacunha.aleia.domain.SeedBooksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<SaveBooksRepository> { SaveBooksRepositoryImpl(get()) }
    single<BookRemoteRepository> { BookRemoteRepositoryImpl(get()) }

    single<CoroutineScope>(named(APP_SCOPE)) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    single { SeedPreferences(androidContext()) }

    single { SeedBookDataSource(context = androidContext(), moshi = get()) }

    single<SeedBooksRepository> {
        SeedBooksRepositoryImpl(
            dataSource = get(),
            booksDao = get(),
            seedPreferences = get()
        )
    }
}