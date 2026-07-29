package br.com.camilacunha.aleia.domain

import br.com.camilacunha.aleia.domain.model.AddBookResult
import br.com.camilacunha.aleia.domain.model.Book

interface SaveBooksRepository {
    suspend fun addBook(book: Book): AddBookResult
    suspend fun getAllBooks(): List<Book>
}