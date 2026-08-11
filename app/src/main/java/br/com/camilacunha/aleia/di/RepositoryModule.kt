package br.com.camilacunha.aleia.di

import br.com.camilacunha.aleia.data.repository.BookRemoteRepositoryImpl
import br.com.camilacunha.aleia.data.repository.SaveBooksRepositoryImpl
import br.com.camilacunha.aleia.domain.BookRemoteRepository
import br.com.camilacunha.aleia.domain.SaveBooksRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<SaveBooksRepository> { SaveBooksRepositoryImpl(get()) }
    single<BookRemoteRepository> { BookRemoteRepositoryImpl(get()) }
}