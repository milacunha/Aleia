package br.com.camilacunha.aleia

import br.com.camilacunha.aleia.data.local.model.BookEntity
import br.com.camilacunha.aleia.domain.model.Book

object DataFactoryTest {

    fun book(
        id: Int = 0,
        title: String = "O Hobbit",
        author: String? = "J.R.R. Tolkien",
        genre: String? = "Fantasia",
        coverUrl: String? = "https://example.com/cover.jpg",
        isRead: Boolean = false
    ) = Book(
        id = id,
        title = title,
        author = author,
        genre = genre,
        coverUrl = coverUrl,
        isRead = isRead
    )

    fun entity(
        id: Int = 0,
        title: String = "O Hobbit",
        author: String? = "J.R.R. Tolkien",
        genre: String? = "Fantasia",
        coverUrl: String? = "https://example.com/cover.jpg",
        isRead: Boolean = false
    ) = BookEntity(
        id = id,
        title = title,
        author = author,
        genre = genre,
        coverUrl = coverUrl,
        isRead = isRead
    )
}