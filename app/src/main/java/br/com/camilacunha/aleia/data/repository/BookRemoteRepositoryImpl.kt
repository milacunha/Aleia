package br.com.camilacunha.aleia.data.repository

import android.util.Log
import br.com.camilacunha.aleia.BuildConfig
import br.com.camilacunha.aleia.data.remote.api.GoogleBooksApi
import br.com.camilacunha.aleia.data.remote.model.Volume
import br.com.camilacunha.aleia.domain.BookRemoteRepository

class BookRemoteRepositoryImpl(
    private val api: GoogleBooksApi
) : BookRemoteRepository {

    override suspend fun searchBookByTitle(title: String): Volume? {
        return try {
            val query = "intitle:$title"
            val apiKey = BuildConfig.GOOGLE_BOOKS_API_KEY.takeIf { it.isNotEmpty() }

            Log.d("BookRemoteRepository", "Buscando livro na API: $query")

            val response = api.searchBooks(
                query = query,
                maxResults = 1,
                apiKey = apiKey
            )

            val volume = response.items?.firstOrNull()
            if (volume == null) {
                Log.d("BookRemoteRepository", "Nenhum livro encontrado para: $title")
            } else {
                Log.d("BookRemoteRepository", "Livro encontrado: ${volume.volumeInfo?.title}")
            }
            volume
        } catch (e: Exception) {
            Log.d("BookRemoteRepository", "Erro ao buscar livro na API: $title")
            throw e
        }
    }

    override suspend fun fetchCoverAndGenre(title: String): Pair<String?, String?>? {
        val volume = searchBookByTitle(title) ?: return null
        val coverUrl = volume.volumeInfo?.imageLinks?.thumbnail
        val genre = volume.volumeInfo?.genres?.firstOrNull()
        return Pair(coverUrl, genre)
    }
}