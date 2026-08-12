package br.com.camilacunha.aleia.domain

import br.com.camilacunha.aleia.data.remote.model.Volume
import br.com.camilacunha.aleia.domain.model.Book

interface BookRemoteRepository {
    suspend fun searchBooksByTitle(title: String): List<Book>
    suspend fun searchBookByTitle(title: String): Volume?
    suspend fun fetchCoverAndGenre(title: String): Pair<String?, String?>?
}