package br.com.camilacunha.aleia.di

import br.com.camilacunha.aleia.data.local.database.AppDatabase
import br.com.camilacunha.aleia.ui.BookViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().bookDao() }

    viewModel { BookViewModel(get(), get()) }
}