package br.com.camilacunha.aleia.ui

import androidx.compose.runtime.Composable
import br.com.camilacunha.aleia.ui.screen.BookScreenContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookScreen() {

    val viewModel: BookViewModel = koinViewModel()

    BookScreenContent(
        onClick = { viewModel.addMockBooks() }
    )
}