package br.com.camilacunha.aleia.data.repository

import br.com.camilacunha.aleia.DataFactoryTest.book
import br.com.camilacunha.aleia.DataFactoryTest.entity
import br.com.camilacunha.aleia.data.local.database.BooksDao
import br.com.camilacunha.aleia.domain.model.AddBookResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveBooksRepositoryImplTest {

    private val dao: BooksDao = mockk()
    private val repository = SaveBooksRepositoryImpl(dao)

    @Test
    fun `when DAO inserts with success, then addBook returns Success`() = runTest {
        coEvery { dao.insertBook(any()) } returns 1L

        val result = repository.addBook(book(title = "O Hobbit"))

        assertEquals(AddBookResult.Success, result)
        coVerify(exactly = 1) { dao.insertBook(any()) }
    }

    @Test
    fun `when DAO ignores insert, then addBook returns AlreadyExists`() = runTest {
        coEvery { dao.insertBook(any()) } returns -1L

        val result = repository.addBook(book(title = "O Hobbit"))

        assertEquals(AddBookResult.AlreadyExists, result)
    }

    @Test
    fun `when getUnreadBooksByGenre is called, then DAO is queried and result is mapped`() =
        runTest {
            val entities = listOf(
                entity(id = 1, title = "O Hobbit", genre = "Fantasia"),
                entity(id = 2, title = "O Silmarillion", genre = "Fantasia")
            )
            coEvery { dao.getUnreadBooksByGenre("Fantasia") } returns entities

            val books = repository.getUnreadBooksByGenre("Fantasia")

            assertEquals(2, books.size)
            assertEquals("O Hobbit", books[0].title)
            assertEquals("O Silmarillion", books[1].title)
            coVerify(exactly = 1) { dao.getUnreadBooksByGenre("Fantasia") }
        }
}