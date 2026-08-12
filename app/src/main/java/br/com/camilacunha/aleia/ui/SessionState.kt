package br.com.camilacunha.aleia.ui

import br.com.camilacunha.aleia.domain.model.Book

data class SessionState(
    val unreadBooks: List<Book> = emptyList(),
    val shownIds: Set<Int> = emptySet(),
    val activeFilter: String? = null
)