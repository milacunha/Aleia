package br.com.camilacunha.aleia.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.camilacunha.aleia.R
import br.com.camilacunha.aleia.ui.BookViewModel
import br.com.camilacunha.aleia.ui.state.AddBookUiState
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookBottomSheet() {

    val viewModel: BookViewModel = koinViewModel()

    val uiState by viewModel.addBookUiState.collectAsStateWithLifecycle()
    val isVisible by viewModel.isAddSheetVisible.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var titleInput by remember { mutableStateOf("") }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissAddSheet() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Adicionar livro",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text("Título do livro") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            if (titleInput.isNotBlank()) {
                                viewModel.searchBookForAddition(titleInput)
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (uiState) {
                    AddBookUiState.Idle -> {
                        Button(
                            onClick = {
                                keyboardController?.hide()
                                viewModel.searchBookForAddition(titleInput)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = titleInput.isNotBlank()
                        ) {
                            Text("Buscar e adicionar")
                        }
                    }

                    AddBookUiState.Loading -> {
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.height(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Buscando...")
                        }
                    }

                    is AddBookUiState.Success -> {
                        val book = uiState as AddBookUiState.Success
                        Text(
                            text = "✅ Livro encontrado! Adicionado com sucesso.",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text("Título: ${book.title}")
                        book.genre?.let { Text("Gênero: $it") }
                    }

                    is AddBookUiState.Error -> {
                        val error = uiState as AddBookUiState.Error

                        Text(
                            text = error.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        error.title?.let {
                            Button(
                                onClick = { viewModel.addBookWithoutMetadata(it) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Adicionar assim mesmo")
                            }
                        }

                    }

                    is AddBookUiState.SearchResults -> {
                        val results = uiState as AddBookUiState.SearchResults
                        Column {
                            Button(
                                onClick = {
                                    viewModel.resetAddState()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Nova busca")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Selecione um livro:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            results.books.forEach { book ->
                                //
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.saveSelectedBook(book) }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Capa
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(book.coverUrl)
                                            .crossfade(true)
                                            .placeholder(R.drawable.book) // ou um placeholder genérico
                                            .error(R.drawable.book)
                                            .build(),
                                        contentDescription = "Capa do livro ${book.title}",
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    // Título e autor
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = book.title,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        if (book.author != null) {
                                            Text(
                                                text = book.author,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                                // Linha divisória (exceto no último)
                                if (results.books.last() != book) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                }
                                //
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddBookBottomSheetPreview() {
    AleiaTheme {
        /*TODO("fazer a preview do bottom sheet")*/
    }
}