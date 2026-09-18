package br.com.camilacunha.aleia.di

import br.com.camilacunha.aleia.data.local.database.BooksDao
import br.com.camilacunha.aleia.data.repository.SaveBooksRepositoryImpl
import br.com.camilacunha.aleia.domain.BookRemoteRepository
import br.com.camilacunha.aleia.domain.SaveBooksRepository
import br.com.camilacunha.aleia.ui.BookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun testModule(
    dao: BooksDao,
    remoteRepository: BookRemoteRepository
) = module {

    single { dao }

    single<SaveBooksRepository> { SaveBooksRepositoryImpl(get()) }

    single<BookRemoteRepository> { remoteRepository }

    viewModel { BookViewModel(get(), get()) }
}