package br.com.camilacunha.aleia.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.camilacunha.aleia.ui.BookViewModel
import br.com.camilacunha.aleia.ui.state.FilterUiState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterGenreBottomSheet() {

    val viewModel: BookViewModel = koinViewModel()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val genres by viewModel.genres.collectAsStateWithLifecycle(initialValue = emptyList())
    val isVisible by viewModel.isFilterSheetVisible.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissFilterSheet() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Filtrar por gênero",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (filterState is FilterUiState.Active) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.clearFilter()
                                viewModel.dismissFilterSheet()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limpar filtro")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (genres.isEmpty()) {
                    Text(
                        text = "Nenhum gênero disponível",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    LazyColumn {
                        items(genres) { genre ->
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = genre,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        coroutineScope.launch {
                                            viewModel.applyFilter(genre)
                                            viewModel.dismissFilterSheet()
                                        }
                                    }
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}