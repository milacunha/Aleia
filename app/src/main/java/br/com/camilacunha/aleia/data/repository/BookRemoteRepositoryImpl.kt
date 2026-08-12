package br.com.camilacunha.aleia.data.repository

import android.util.Log
import br.com.camilacunha.aleia.BuildConfig
import br.com.camilacunha.aleia.data.remote.api.GoogleBooksApi
import br.com.camilacunha.aleia.data.remote.model.Volume
import br.com.camilacunha.aleia.domain.BookRemoteRepository
import br.com.camilacunha.aleia.domain.model.Book
import java.text.Normalizer

class BookRemoteRepositoryImpl(
    private val api: GoogleBooksApi
) : BookRemoteRepository {

    private val tag = "BookRemoteRepository"

    override suspend fun searchBookByTitle(title: String): Volume? {
        return try {
            val primaryLang = detectLanguage(title)
            Log.d(tag, "Idioma detectado para '$title': $primaryLang")

            var volume = searchWithLanguage(title, primaryLang)

            if (volume == null) {
                val fallbackLang = if (primaryLang == "pt") "en" else "pt"
                Log.d(tag, "Fallback para idioma: $fallbackLang")
                volume = searchWithLanguage(title, fallbackLang)
            }

            if (volume == null) {
                Log.d(tag, "Último recurso: buscando sem restrição de idioma")
                volume = searchWithLanguage(title, null)
            }

            volume
        } catch (e: Exception) {
            Log.d(tag, "Erro ao buscar livro na API: $title")
            throw e
        }
    }

    override suspend fun searchBooksByTitle(title: String): List<Book> {
        return try {
            val query = "intitle:\"$title\""
            val apiKey = BuildConfig.GOOGLE_BOOKS_API_KEY.takeIf { it.isNotEmpty() }

            Log.d(tag, "Buscando lista de livros: $query (maxResults=5)")

            val response = api.searchBooks(
                query = query,
                maxResults = 5,
                apiKey = apiKey,
                langRestrict = detectLanguage(title)
            )

            val volumes = response.items ?: emptyList()
            Log.d(tag, "Total retornado: ${volumes.size}")

            val books = volumes
                .filter { it.volumeInfo?.imageLinks?.thumbnail != null }
                .take(3)
                .map { volume ->
                    val info = volume.volumeInfo
                    Book(
                        title = info?.title ?: title,
                        author = info?.authors?.joinToString(),
                        genre = info?.genres?.firstOrNull(),
                        coverUrl = info?.imageLinks?.thumbnail
                    )
                }

            Log.d(tag, "Livros com capa encontrados: ${books.size}")
            books
        } catch (e: Exception) {
            Log.e(tag, "Erro ao buscar lista", e)
            emptyList()
        }
    }

    override suspend fun fetchCoverAndGenre(title: String): Pair<String?, String?>? {
        val volume = searchBookByTitle(title) ?: return null
        val coverUrl = volume.volumeInfo?.imageLinks?.thumbnail
        val genre = volume.volumeInfo?.genres?.firstOrNull()
        return Pair(coverUrl, genre)
    }

    private fun detectLanguage(title: String): String {
        val portugueseIndicators = listOf(
            "ão", "ões", "ães",
            "á", "é", "í", "ó", "ú", "â", "ê", "ô", "ç", "e a", "e o",
            "de", "do", "da", "dos", "das",
            "que", "com", "para", "por", "uma", "um",
            "enigma", "príncipe", "coração", "revolução"
        )

        val normalized = Normalizer.normalize(title, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}"), "")

        val hasPortugueseIndicator = portugueseIndicators.any { indicator ->
            normalized.contains(indicator, ignoreCase = true)
        }

        return if (hasPortugueseIndicator) "pt" else "en"
    }

    private suspend fun searchWithLanguage(title: String, lang: String?): Volume? {
        val query = "intitle:\"$title\""
        val apiKey = BuildConfig.GOOGLE_BOOKS_API_KEY.takeIf { it.isNotEmpty() }

        Log.d(tag, "Buscando com langRestrict=$lang: $query")

        val response = api.searchBooks(
            query = query,
            maxResults = 1,
            apiKey = apiKey,
            langRestrict = lang
        )

        return response.items?.firstOrNull()
    }
}