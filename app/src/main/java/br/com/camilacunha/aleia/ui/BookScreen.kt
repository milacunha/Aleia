package br.com.camilacunha.aleia.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.camilacunha.aleia.ui.screen.BookScreenContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookScreen() {

    val viewModel: BookViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is BookUiState.Loading -> {
            // Mostrar loading
        }

        is BookUiState.Success -> {
            val book = (uiState as BookUiState.Success).book

            BookScreenContent(
                bookTitle = book.title,
                bookGenre = book.genre.orEmpty(),
                bookCover = null,
                onTryAgain = { viewModel.randomizeAgain() },
                onAccepted = { viewModel.acceptSuggestion() },
                onFilter = { /* TODO() */ },
                onAddBook = { /* TODO() */ viewModel.addMockBooks() }
            )
        }

        is BookUiState.Empty,
        is BookUiState.Error -> {
            val message = (uiState as BookUiState.Empty).message
            BookScreenContent(
                bookTitle = message,
                bookGenre = "",
                bookCover = null,
                onTryAgain = { viewModel.randomizeAgain() },
                onAccepted = { viewModel.acceptSuggestion() },
                onFilter = { },
                onAddBook = { viewModel.addMockBooks() }
            )
        }
    }
}