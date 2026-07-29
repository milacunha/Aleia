package br.com.camilacunha.aleia.domain.model

sealed class AddBookResult {
    data object Success : AddBookResult()
    data object AlreadyExists : AddBookResult()
    data class Error(val message: String) : AddBookResult()
}