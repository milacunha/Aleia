package br.com.camilacunha.aleia.di

import android.content.Context
import androidx.room.Room
import br.com.camilacunha.aleia.data.local.database.AppDatabase

fun createInMemoryDatabase(context: Context): AppDatabase =
    Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()