package br.com.camilacunha.aleia.domain.model

data class Book(
    val id: Int = 0,
    val title: String,
    val author: String? = null,
    val genre: String? = null,
    val coverUrl: String? = null,
    val isRead: Boolean = false
)