package br.com.camilacunha.aleia.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.camilacunha.aleia.ui.screen.AddBookBottomSheet
import br.com.camilacunha.aleia.ui.screen.BookScreenContent
import br.com.camilacunha.aleia.ui.screen.FilterGenreBottomSheet
import br.com.camilacunha.aleia.ui.state.BookUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookScreen() {

    val viewModel: BookViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()

    when (uiState) {
        is BookUiState.Loading -> {
            /* TODO("tela de loading") */
        }

        is BookUiState.Success -> {
            val book = (uiState as BookUiState.Success).book

            BookScreenContent(
                bookTitle = book.title,
                bookGenre = book.genre.orEmpty(),
                bookCover = book.coverUrl,
                filterState = filterState,
                onTryAgain = { viewModel.randomizeAgain() },
                onAccepted = { viewModel.acceptSuggestion() },
                onFilter = { viewModel.showFilterSheet() },
                onAddBook = { viewModel.showAddSheet() }
            )
        }

        is BookUiState.Empty,
        is BookUiState.Error -> {
            val message = (uiState as BookUiState.Empty).message
            /* TODO("tela de erro ou lista vazia") */
            BookScreenContent(
                bookTitle = message,
                bookGenre = "",
                bookCover = null,
                filterState = filterState,
                onTryAgain = { /* esconder botao */ },
                onAccepted = { /* esconder botao */ },
                onFilter = { /* esconder botao */ },
                onAddBook = { viewModel.showAddSheet() }
            )
        }
    }

    AddBookBottomSheet()
    FilterGenreBottomSheet()
}