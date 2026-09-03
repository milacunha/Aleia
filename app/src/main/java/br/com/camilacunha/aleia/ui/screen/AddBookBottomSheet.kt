package br.com.camilacunha.aleia.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.camilacunha.aleia.R
import br.com.camilacunha.aleia.domain.model.Book
import br.com.camilacunha.aleia.ui.BookViewModel
import br.com.camilacunha.aleia.ui.component.NeoBrutButton
import br.com.camilacunha.aleia.ui.component.NeoBrutTextField
import br.com.camilacunha.aleia.ui.state.AddBookUiState
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import br.com.camilacunha.aleia.ui.theme.Black
import br.com.camilacunha.aleia.ui.theme.DarkGray
import br.com.camilacunha.aleia.ui.theme.White
import br.com.camilacunha.aleia.ui.theme.YellowNeo
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
    var titleInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (isVisible) {
        ModalBottomSheet(
            modifier = Modifier.padding(12.dp),
            onDismissRequest = { viewModel.dismissAddSheet() },
            sheetState = sheetState,
            containerColor = Color.Transparent,
            shape = RoundedCornerShape(0.dp)
        ) {
            NeoBackground(
                uiState = uiState,
                titleInput = titleInput,
                onTitleChange = { titleInput = it },
                onSearch = {
                    keyboardController?.hide()
                    viewModel.searchBookForAddition(titleInput)
                },
                onAddWithoutMetadata = { viewModel.addBookWithoutMetadata(it) },
                onSelectBook = { viewModel.saveSelectedBook(it) },
                onResetSearch = { viewModel.resetAddState() }
            )
        }
    }
}

@Composable
fun NeoBackground(
    backgroundColor: Color = White,
    borderColor: Color = Black,
    borderWidth: Dp = 3.dp,
    uiState: AddBookUiState,
    titleInput: String,
    onTitleChange: (String) -> Unit,
    onSearch: () -> Unit,
    onAddWithoutMetadata: (String) -> Unit,
    onSelectBook: (Book) -> Unit,
    onResetSearch: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundColor)
            .border(borderWidth, borderColor)
    ) {
        AddBottomSheetContent(
            uiState = uiState,
            titleInput = titleInput,
            onTitleChange = onTitleChange,
            onSearch = onSearch,
            onAddWithoutMetadata = onAddWithoutMetadata,
            onSelectBook = onSelectBook,
            onResetSearch = onResetSearch
        )
    }
}


@Composable
private fun AddBottomSheetContent(
    uiState: AddBookUiState,
    titleInput: String,
    onTitleChange: (String) -> Unit,
    onSearch: () -> Unit,
    onAddWithoutMetadata: (String) -> Unit,
    onSelectBook: (Book) -> Unit,
    onResetSearch: () -> Unit,
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
            text = stringResource(R.string.add_book).uppercase(),
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.space_grotesk)),
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        NeoBrutTextField(
            value = titleInput,
            onValueChange = onTitleChange,
            label = stringResource(R.string.title_book),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSearch() })
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            AddBookUiState.Idle -> {
                NeoBrutButton(
                    modifier = Modifier.size(300.dp, 40.dp),
                    onClick = onSearch,
                    text = stringResource(R.string.search_book).uppercase(),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    backgroundColor = YellowNeo
                )
            }

            AddBookUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.wrapContentSize(),
                    color = YellowNeo
                )
            }

            is AddBookUiState.Success -> {
                Text(
                    text = stringResource(R.string.found_book).uppercase(),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    color = DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (uiState.genre == null) {
                    Text(
                        text = stringResource(R.string.title, uiState.title).uppercase(),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.space_grotesk)),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center,
                        color = Black,
                        modifier = Modifier
                            .background(
                                YellowNeo.copy(alpha = 0.15f),
                                RoundedCornerShape(0.dp)
                            )
                            .padding(4.dp)
                    )
                } else {
                    Text(
                        text = stringResource(
                            R.string.title_and_genre,
                            uiState.title,
                            uiState.genre
                        ).uppercase(),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.space_grotesk)),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center,
                        color = Black,
                        modifier = Modifier
                            .background(
                                YellowNeo.copy(alpha = 0.15f),
                                RoundedCornerShape(0.dp)
                            )
                            .padding(4.dp)
                    )
                }
            }

            is AddBookUiState.Error -> {
                Text(
                    text = uiState.message.uppercase(),
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    color = DarkGray
                )

                uiState.title?.let {
                    NeoBrutButton(
                        modifier = Modifier.size(300.dp, 40.dp),
                        onClick = { onAddWithoutMetadata(it) },
                        text = stringResource(R.string.not_found_add_book).uppercase(),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.space_grotesk)),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        backgroundColor = YellowNeo
                    )
                }

            }

            is AddBookUiState.SearchResults -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NeoBrutButton(
                        modifier = Modifier.size(300.dp, 40.dp),
                        onClick = onResetSearch,
                        text = stringResource(R.string.search_new_book).uppercase(),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.space_grotesk)),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        backgroundColor = YellowNeo
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.select_book).uppercase(),
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.space_grotesk)),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )

                    uiState.books.forEach { book ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectBook(book) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(book.coverUrl)
                                    .crossfade(true)
                                    .placeholder(R.drawable.book)
                                    .error(R.drawable.book)
                                    .build(),
                                contentDescription = stringResource(
                                    R.string.cover_book,
                                    book.title
                                ),
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = book.title.uppercase(),
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 2.sp,
                                    textAlign = TextAlign.Start
                                )
                                if (book.author != null) {
                                    Text(
                                        text = book.author.uppercase(),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(top = 4.dp),
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 2.sp,
                                        textAlign = TextAlign.Start,
                                        color = DarkGray
                                    )
                                }
                            }
                        }
                        if (uiState.books.last() != book) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddBookBottomSheetIdlePreview() {
    AleiaTheme {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            AddBottomSheetContent(
                uiState = AddBookUiState.Idle,
                titleInput = "",
                onTitleChange = {},
                onSearch = {},
                onAddWithoutMetadata = {},
                onSelectBook = {},
                onResetSearch = {}
            )

            Spacer(modifier = Modifier.height(100.dp))
            AddBottomSheetContent(
                uiState = AddBookUiState.Idle,
                titleInput = "Duna 2",
                onTitleChange = {},
                onSearch = {},
                onAddWithoutMetadata = {},
                onSelectBook = {},
                onResetSearch = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddBookBottomSheetLoadingPreview() {
    AleiaTheme {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            AddBottomSheetContent(
                uiState = AddBookUiState.Loading,
                titleInput = "Duna 2",
                onTitleChange = {},
                onSearch = {},
                onAddWithoutMetadata = {},
                onSelectBook = {},
                onResetSearch = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddBookBottomSheetErrorPreview() {
    AleiaTheme {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            AddBottomSheetContent(
                uiState = AddBookUiState.Error("Livro já existe na biblioteca!", "Duna 2"),
                titleInput = "Duna 2",
                onTitleChange = {},
                onSearch = {},
                onAddWithoutMetadata = {},
                onSelectBook = {},
                onResetSearch = {}
            )

            Spacer(modifier = Modifier.height(100.dp))
            AddBottomSheetContent(
                uiState = AddBookUiState.Error("Livro já existe na biblioteca!"),
                titleInput = "Duna 2",
                onTitleChange = {},
                onSearch = {},
                onAddWithoutMetadata = {},
                onSelectBook = {},
                onResetSearch = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddBookBottomSheetSuccessPreview() {
    AleiaTheme {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            AddBottomSheetContent(
                uiState = AddBookUiState.Success("Half Bad", "J.K. Rolling", "Ficção", null),
                titleInput = "Duna 2",
                onTitleChange = {},
                onSearch = {},
                onAddWithoutMetadata = {},
                onSelectBook = {},
                onResetSearch = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddBookBottomSheetSearchPreview() {
    AleiaTheme {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            AddBottomSheetContent(
                uiState = AddBookUiState.SearchResults(
                    books = listOf(
                        Book(id = 1, title = "Senhor dos Aneis 1", "Tolkien"),
                        Book(id = 1, title = "Senhor dos Aneis: A sociedade do anel"),
                        Book(id = 1, title = "A sociedade do anel (Senhor dos Aneis)")
                    )
                ),
                titleInput = "Duna 2",
                onTitleChange = {},
                onSearch = {},
                onAddWithoutMetadata = {},
                onSelectBook = {},
                onResetSearch = {}
            )
        }
    }
}