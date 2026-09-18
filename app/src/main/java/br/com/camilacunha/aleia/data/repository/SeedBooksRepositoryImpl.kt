package br.com.camilacunha.aleia.data.repository

import android.util.Log
import br.com.camilacunha.aleia.data.local.database.BooksDao
import br.com.camilacunha.aleia.data.local.seed.SeedBookDataSource
import br.com.camilacunha.aleia.data.local.seed.SeedPreferences
import br.com.camilacunha.aleia.data.mapper.toEntity
import br.com.camilacunha.aleia.domain.SeedBooksRepository

class SeedBooksRepositoryImpl(
    private val dataSource: SeedBookDataSource,
    private val booksDao: BooksDao,
    private val seedPreferences: SeedPreferences
) : SeedBooksRepository {

    private val tag = this::class.java.simpleName

    override suspend fun seedIfNeeded() {
        if (seedPreferences.isSeedDone()) {
            Log.d(tag, "Seed já executado anteriormente. Pulando.")
            return
        }

        val dtos = dataSource.readSeedBooks()
        if (dtos.isEmpty()) {
            Log.e(tag, "Nenhum livro encontrado no JSON. Flag NÃO será marcada.")
            return
        }

        try {
            val entities = dtos.map { it.toEntity() }
            val results = booksDao.insertBooks(entities)
            val inserted = results.count { it != -1L }
            val ignored = results.size - inserted

            Log.d(tag, "Seed concluído: $inserted inseridos, $ignored ignorados (duplicados).")
            seedPreferences.markSeedDone()
        } catch (e: Exception) {
            Log.e(tag, "Erro ao inserir livros do seed no Room", e)
        }
    }
}