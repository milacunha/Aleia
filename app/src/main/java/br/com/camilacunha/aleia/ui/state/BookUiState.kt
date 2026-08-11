package br.com.camilacunha.aleia.ui.state

import br.com.camilacunha.aleia.domain.model.Book

sealed class BookUiState {
    data object Loading : BookUiState()
    data class Success(val book: Book) : BookUiState()
    data class Empty(val message: String = "Nenhum livro disponível") : BookUiState()
    data class Error(val message: String = "Erro! Tente novamente...") : BookUiState()
}