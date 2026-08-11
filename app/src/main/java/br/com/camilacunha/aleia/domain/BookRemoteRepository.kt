package br.com.camilacunha.aleia.domain

import br.com.camilacunha.aleia.data.remote.model.Volume

interface BookRemoteRepository {
    suspend fun searchBookByTitle(title: String): Volume?
    suspend fun fetchCoverAndGenre(title: String): Pair<String?, String?>?
}