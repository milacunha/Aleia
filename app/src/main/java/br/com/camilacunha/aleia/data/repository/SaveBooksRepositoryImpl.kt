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

    private val tag = "SaveBooksRepositoryImpl"

    override suspend fun addBook(book: Book): AddBookResult {
        val bookEntity = book.toEntity()

        return try {
            val insertedId = bookDao.insertBook(bookEntity)
            if (insertedId != -1L) {
                Log.d(tag, "Inserido: ${bookEntity.title}")
                AddBookResult.Success
            } else {
                Log.d(tag, "Esse livro já foi salvo: ${bookEntity.title}")
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

    override suspend fun getDistinctGenres(): List<String> {
        return bookDao.getDistinctGenres()
    }

    override suspend fun getUnreadBooksByGenre(genre: String): List<Book> {
        return bookDao.getUnreadBooksByGenre(genre).map { it.toDomain() }
    }

    override suspend fun getBooksMissingMetadata(): List<Book> {
        return bookDao.getBooksMissingMetadata().map { it.toDomain() }
    }

    override suspend fun updateBookCoverAndGenre(
        id: Int,
        coverUrl: String?,
        genre: String?
    ): Boolean {
        return try {
            val affectedRows = bookDao.updateBookCoverAndGenre(id, coverUrl, genre)
            val success = affectedRows > 0
            if (success) {
                Log.d(tag, "Atualizado livro ID $id: cover=$coverUrl, genre=$genre")
            } else {
                Log.d(tag, "Falha ao atualizar livro ID $id (não encontrado)")
            }
            success
        } catch (e: Exception) {
            Log.e(tag, "Erro ao atualizar livro ID $id", e)
            false
        }
    }
}