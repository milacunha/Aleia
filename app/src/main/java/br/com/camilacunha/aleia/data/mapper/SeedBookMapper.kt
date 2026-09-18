package br.com.camilacunha.aleia.data.mapper

import br.com.camilacunha.aleia.data.local.model.BookEntity
import br.com.camilacunha.aleia.data.local.seed.SeedBookDto

fun SeedBookDto.toEntity(): BookEntity = BookEntity(
    title = title,
    author = author,
    genre = genre,
    coverUrl = coverUrl,
    isRead = false
)