package br.com.camilacunha.aleia.ui.state

import br.com.camilacunha.aleia.domain.model.Book

sealed class AddBookUiState {
    data object Idle : AddBookUiState()
    data object Loading : AddBookUiState()
    data class Success(
        val title: String,
        val author: String?,
        val genre: String?,
        val coverUrl: String?
    ) : AddBookUiState()

    data class Error(val message: String, val title: String? = null) : AddBookUiState()
    data class SearchResults(val books: List<Book>) : AddBookUiState()
}