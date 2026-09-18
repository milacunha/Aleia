package br.com.camilacunha.aleia.data.repository

import br.com.camilacunha.aleia.data.RemoteTestHelpers.mockVolume
import br.com.camilacunha.aleia.data.RemoteTestHelpers.responseWith
import br.com.camilacunha.aleia.data.remote.api.GoogleBooksApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BookRemoteRepositoryImplTest {

    private val api: GoogleBooksApi = mockk()
    private val repository = BookRemoteRepositoryImpl(api)

    @Test
    fun `when API returns volumes with covers, then searchBooksByTitle maps them to books`() =
        runTest {
            val volume = mockVolume(
                title = "O Hobbit",
                authors = listOf("Tolkien"),
                genres = listOf("Fantasia"),
                thumbnail = "https://example.com/cover.jpg"
            )
            coEvery { api.searchBooks(any(), any(), any(), any()) } returns
                    responseWith(listOf(volume))

            val books = repository.searchBooksByTitle("O Hobbit")

            assertEquals(1, books.size)
            assertEquals("O Hobbit", books[0].title)
            assertEquals("Tolkien", books[0].author)
            assertEquals("Fantasia", books[0].genre)
            assertEquals("https://example.com/cover.jpg", books[0].coverUrl)
        }

    @Test
    fun `when API throws, then searchBooksByTitle returns empty list`() = runTest {
        coEvery {
            api.searchBooks(
                any(),
                any(),
                any(),
                any()
            )
        } throws RuntimeException("no network")

        val books = repository.searchBooksByTitle("O Hobbit")

        assertTrue(books.isEmpty())
    }

    @Test
    fun `when API finds nothing, then fetchCoverAndGenre returns null`() = runTest {
        coEvery { api.searchBooks(any(), any(), any(), any()) } returns responseWith(emptyList())

        val result = repository.fetchCoverAndGenre("Livro Inexistente")

        assertNull(result)
    }

}