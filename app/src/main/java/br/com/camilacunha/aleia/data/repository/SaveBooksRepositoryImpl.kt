package br.com.camilacunha.aleia.data.repository

import android.util.Log
import br.com.camilacunha.aleia.data.local.database.BooksDao
import br.com.camilacunha.aleia.data.mapper.toDomain
import br.com.camilacunha.aleia.data.mapper.toEntity
import br.com.camilacunha.aleia.domain.SaveBooksRepository
import br.com.camilacunha.aleia.domain.model.AddBookResult
import br.com.camilacunha.aleia.domain.model.Book

class SaveBooksRepositoryImpl(
    private val bookDao: BooksDao
) : SaveBooksRepository {

    override suspend fun addBook(book: Book): AddBookResult {
        val bookEntity = book.toEntity()

        return try {
            val insertedId = bookDao.insertBook(bookEntity)
            if (insertedId != -1L) {
                Log.d("SaveBooksRepositoryImpl", "Inserido: ${bookEntity.title}")
                AddBookResult.Success
            } else {
                Log.d("SaveBooksRepositoryImpl", "Esse livro já foi salvo: ${bookEntity.title}")
                AddBookResult.AlreadyExists
            }
        } catch (e: Exception) {
            AddBookResult.Error(message = e.message ?: "Erro ao salvar livro")
        }
    }

    override suspend fun getAllUnreadBooks(): List<Book> {
        return bookDao.getAllUnreadBooks().map { it.toDomain() }
    }

    override suspend fun markAsRead(bookId: Int) {
        bookDao.markAsRead(bookId)
    }
}