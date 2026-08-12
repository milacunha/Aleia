package br.com.camilacunha.aleia.data.remote.api

import br.com.camilacunha.aleia.data.remote.model.GoogleBookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 1,
        @Query("key") apiKey: String? = null,
        @Query("langRestrict") langRestrict: String? = null
    ): GoogleBookResponse
}