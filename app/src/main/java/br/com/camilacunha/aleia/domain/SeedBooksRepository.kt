package br.com.camilacunha.aleia.domain

interface SeedBooksRepository {
    suspend fun seedIfNeeded()
}