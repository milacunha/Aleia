package br.com.camilacunha.aleia.data.mapper

import br.com.camilacunha.aleia.data.local.model.BookEntity
import br.com.camilacunha.aleia.domain.model.Book

fun BookEntity.toDomain() = Book(
    id = id,
    title = title,
    author = author,
    genre = genre,
    coverUrl = coverUrl,
    isRead = isRead
)

fun Book.toEntity() = BookEntity(
    id = id,
    title = title,
    author = author,
    genre = genre,
    coverUrl = coverUrl,
    isRead = isRead
)