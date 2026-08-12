package br.com.camilacunha.aleia.ui.state

sealed class FilterUiState {
    data object Inactive : FilterUiState()
    data class Active(val genre: String) : FilterUiState()
}