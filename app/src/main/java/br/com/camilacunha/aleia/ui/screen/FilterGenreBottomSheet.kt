package br.com.camilacunha.aleia.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.camilacunha.aleia.R
import br.com.camilacunha.aleia.ui.BookViewModel
import br.com.camilacunha.aleia.ui.component.NeoBrutButton
import br.com.camilacunha.aleia.ui.state.FilterUiState
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import br.com.camilacunha.aleia.ui.theme.AquaNeo
import br.com.camilacunha.aleia.ui.theme.Black
import br.com.camilacunha.aleia.ui.theme.DarkGray
import br.com.camilacunha.aleia.ui.theme.White
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
            modifier = Modifier.padding(12.dp),
            sheetState = sheetState,
            containerColor = Color.Transparent,
            shape = RoundedCornerShape(0.dp)
        ) {
            FilterGenreBottomSheetContent(
                filterState, genres, dismissOnClick = {
                    coroutineScope.launch {
                        viewModel.clearFilter()
                        viewModel.dismissFilterSheet()
                    }
                },
                selectedGenreOnClick = {
                    coroutineScope.launch {
                        viewModel.applyFilter(it)
                        viewModel.dismissFilterSheet()
                    }
                }
            )
        }
    }
}

@Composable
private fun FilterGenreBottomSheetContent(
    filterState: FilterUiState,
    genres: List<String>,
    dismissOnClick: () -> Unit,
    selectedGenreOnClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White)
            .border(3.dp, Black)
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.filter_by_genre).uppercase(),
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.space_grotesk)),
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filterState is FilterUiState.Active) {
                NeoBrutButton(
                    modifier = Modifier.size(300.dp, 40.dp),
                    onClick = dismissOnClick,
                    text = stringResource(R.string.clean_filter).uppercase(),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    backgroundColor = AquaNeo
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (genres.isEmpty()) {
                Text(
                    text = stringResource(R.string.none_genre_available).uppercase(),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    color = DarkGray
                )
            } else {
                LazyColumn {
                    items(genres) { genre ->
                        ListItem(
                            colors = ListItemDefaults.colors(
                                containerColor = AquaNeo.copy(alpha = 0.15f)
                            ),
                            headlineContent = {
                                Text(
                                    text = genre.uppercase(),
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 2.sp,
                                    textAlign = TextAlign.Center,
                                    color = Black,
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedGenreOnClick(genre)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FilterGenreBottomSheetActivePreview() {
    AleiaTheme {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            FilterGenreBottomSheetContent(
                filterState = FilterUiState.Active("Fantasia"),
                genres = listOf("Fantasia", "Romance", "Ficção Científica", "Política"),
                dismissOnClick = { },
                selectedGenreOnClick = { }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FilterGenreBottomSheetInactivePreview() {
    AleiaTheme {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            FilterGenreBottomSheetContent(
                filterState = FilterUiState.Inactive,
                genres = listOf("Fantasia", "Romance", "Ficção Científica", "Política"),
                dismissOnClick = { },
                selectedGenreOnClick = { }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FilterGenreBottomSheetEmptyPreview() {
    AleiaTheme {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            FilterGenreBottomSheetContent(
                filterState = FilterUiState.Inactive,
                genres = listOf(),
                dismissOnClick = { },
                selectedGenreOnClick = { }
            )
        }
    }
}