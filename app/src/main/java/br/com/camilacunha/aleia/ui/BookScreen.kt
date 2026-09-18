package br.com.camilacunha.aleia.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.camilacunha.aleia.R
import br.com.camilacunha.aleia.ui.screen.AddBookBottomSheet
import br.com.camilacunha.aleia.ui.screen.BookScreenContent
import br.com.camilacunha.aleia.ui.screen.FilterGenreBottomSheet
import br.com.camilacunha.aleia.ui.state.BookUiState
import br.com.camilacunha.aleia.ui.state.EmptyAction
import br.com.camilacunha.aleia.ui.state.EmptyState
import br.com.camilacunha.aleia.ui.state.ErrorState
import br.com.camilacunha.aleia.ui.state.LoadingState
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookScreen() {

    val viewModel: BookViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()

    when (uiState) {
        is BookUiState.Loading -> {
            LoadingState()
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

        is BookUiState.Empty -> {
            val emptyUiState = uiState as BookUiState.Empty

            val (actionText, onAction) = when (emptyUiState.action) {
                EmptyAction.ADD_BOOK -> stringResource(R.string.add_book) to { viewModel.showAddSheet() }
                EmptyAction.RESTART_SESSION -> stringResource(R.string.reload_books) to { viewModel.restartSession() }
            }

            EmptyState(
                message = emptyUiState.message,
                actionText = actionText,
                onAction = onAction,
            )
        }

        is BookUiState.Error -> {
            val message = (uiState as BookUiState.Error).message

            ErrorState(
                message = message,
                onRetry = { viewModel.randomizeAgain() }
            )
        }
    }

    AddBookBottomSheet()
    FilterGenreBottomSheet()
}