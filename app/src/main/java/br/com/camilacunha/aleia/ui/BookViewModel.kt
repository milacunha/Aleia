package br.com.camilacunha.aleia.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.camilacunha.aleia.domain.BookRemoteRepository
import br.com.camilacunha.aleia.domain.SaveBooksRepository
import br.com.camilacunha.aleia.domain.model.AddBookResult
import br.com.camilacunha.aleia.domain.model.Book
import br.com.camilacunha.aleia.ui.state.AddBookUiState
import br.com.camilacunha.aleia.ui.state.BookUiState
import br.com.camilacunha.aleia.ui.state.FilterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val saveBooksRepository: SaveBooksRepository,
    private val remoteRepository: BookRemoteRepository
) : ViewModel() {

    private val tag = "BookViewModel"

    private val _uiState = MutableStateFlow<BookUiState>(BookUiState.Loading)
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    private var sessionState = SessionState()

    //add book bottom sheet
    private val _addBookUiState = MutableStateFlow<AddBookUiState>(AddBookUiState.Idle)
    val addBookUiState: StateFlow<AddBookUiState> = _addBookUiState.asStateFlow()

    private val _isAddSheetVisible = MutableStateFlow(false)
    val isAddSheetVisible: StateFlow<Boolean> = _isAddSheetVisible.asStateFlow()

    //filter bottom sheet
    private val _filterState = MutableStateFlow<FilterUiState>(FilterUiState.Inactive)
    val filterState: StateFlow<FilterUiState> = _filterState.asStateFlow()

    // Filter bottom sheet visibility
    private val _isFilterSheetVisible = MutableStateFlow(false)
    val isFilterSheetVisible: StateFlow<Boolean> = _isFilterSheetVisible.asStateFlow()

    // Available genres list
    private val _genres = MutableStateFlow<List<String>>(emptyList())
    val genres: StateFlow<List<String>> = _genres.asStateFlow()

    init {
        loadBooksAndRandomize()
    }

    private fun loadBooksAndRandomize(filter: String? = null) {
        viewModelScope.launch {
            _uiState.value = BookUiState.Loading
            try {
                val unreadBooks = if (filter == null) {
                    saveBooksRepository.getAllUnreadBooks()
                } else {
                    saveBooksRepository.getUnreadBooksByGenre(filter)
                }
                sessionState = SessionState(
                    unreadBooks = unreadBooks,
                    shownIds = emptySet(), //talvez de problema
                    activeFilter = filter
                )

                _filterState.value = if (filter == null) {
                    FilterUiState.Inactive
                } else {
                    FilterUiState.Active(filter)
                }

                Log.d(tag, "livros carregados (filtro: $filter): ${unreadBooks.size}")

                if (unreadBooks.isEmpty()) {
                    _uiState.value = BookUiState.Empty()
                } else {
                    pickRandomBook()
                }
            } catch (e: Exception) {
                Log.e(tag, "Erro ao carregar livros: ", e)
                _uiState.value = BookUiState.Error("Erro ao carregar: ${e.message}")
            }
        }
    }

    fun randomizeAgain() {
        if (sessionState.unreadBooks.isEmpty()) {
            _uiState.value = BookUiState.Empty()
            return
        }
        pickRandomBook()
    }

    fun acceptSuggestion() {
        viewModelScope.launch {
            val currentBook = (_uiState.value as? BookUiState.Success)?.book ?: return@launch
            try {
                saveBooksRepository.markAsRead(currentBook.id)
                Log.e(tag, "Sugestão aceita: $currentBook")
                sessionState = sessionState.copy(
                    unreadBooks = sessionState.unreadBooks.filter { it.id != currentBook.id },
                    shownIds = sessionState.shownIds - currentBook.id
                )
                if (sessionState.unreadBooks.isEmpty()) {
                    _uiState.value = BookUiState.Empty("Todos os livros foram lidos! 🎉")
                } else {
                    pickRandomBook()
                }
            } catch (e: Exception) {
                Log.e(tag, "Erro ao aceitar sugestão: ", e)
                _uiState.value = BookUiState.Error("Erro ao aceitar sugestão: ${e.message}")
            }
        }
    }

    private fun pickRandomBook() {
        val available = sessionState.unreadBooks.filter { it.id !in sessionState.shownIds }
        Log.d(tag, "Livros disponíveis: $available")
        if (available.isEmpty()) {
            _uiState.value = BookUiState.Empty("Todos os livros já foram mostrados nesta sessão")
        } else {
            val randomBook = available.random()
            Log.d(tag, "Livro sorteado: $randomBook")
            sessionState = sessionState.copy(
                shownIds = sessionState.shownIds + randomBook.id
            )
            _uiState.value = BookUiState.Success(randomBook)
        }
    }

    //add book bottom sheet
    fun showAddSheet() {
        _isAddSheetVisible.value = true
        _addBookUiState.value = AddBookUiState.Idle
    }

    fun dismissAddSheet() {
        _isAddSheetVisible.value = false
        _addBookUiState.value = AddBookUiState.Idle
    }

    fun searchBookForAddition(title: String) {
        if (title.isBlank()) {
            _addBookUiState.value = AddBookUiState.Error("Digite um título válido")
            return
        }

        viewModelScope.launch {
            _addBookUiState.value = AddBookUiState.Loading
            try {
                val volume = remoteRepository.searchBookByTitle(title)
                if (volume != null) {
                    val info = volume.volumeInfo
                    val book = Book(
                        title = info?.title ?: title,
                        author = info?.authors?.joinToString(),
                        genre = info?.genres?.firstOrNull(),
                        coverUrl = info?.imageLinks?.thumbnail
                    )
                    val addResult = saveBooksRepository.addBook(book)
                    when (addResult) {
                        AddBookResult.AlreadyExists -> {
                            _addBookUiState.value =
                                AddBookUiState.Error("Livro já existe na biblioteca")
                        }

                        is AddBookResult.Error -> {
                            _addBookUiState.value =
                                AddBookUiState.Error("Erro ao salvar: ${addResult.message}")
                        }

                        AddBookResult.Success -> {
                            _addBookUiState.value = AddBookUiState.Success(
                                title = info?.title ?: title,
                                author = info?.authors?.joinToString(),
                                genre = info?.genres?.firstOrNull(),
                                coverUrl = info?.imageLinks?.thumbnail
                            )
                            refreshUnreadBooks()
                        }
                    }
                } else {
                    _addBookUiState.value = AddBookUiState.Error("Livro não encontrado na API")
                }
            } catch (e: Exception) {
                Log.e(tag, "Erro ao buscar livro para adição", e)
                _addBookUiState.value = AddBookUiState.Error("Erro ao buscar: ${e.message}")
            }
        }
    }

    //TODO("função será removida depois")
    fun addMockBooks() {
        viewModelScope.launch {
            val mockBooks = listOf(
                Book(title = "Duna", genre = "Ficção Científica"),
                Book(title = "Harry potter e a pedra filosofal", genre = "Fantasia"),
                Book(title = "Segundo sexo", genre = "Feminismo"),
                Book(title = "Half Bad", genre = "Fantasia")
            )
            mockBooks.forEach { book ->
                saveBooksRepository.addBook(book).let { result ->
                    when (result) {
                        AddBookResult.Success -> {
                            Log.d(tag, "Livro adicionado com sucesso!")
                        }

                        AddBookResult.AlreadyExists -> {
                            Log.d(tag, "Livro já existe no banco!")
                        }

                        is AddBookResult.Error -> {
                            Log.d(tag, "Erro genérico: ${result.message}")
                        }
                    }
                }
            }

            val mockBooksSize = saveBooksRepository.getAllUnreadBooks().size
            Log.d(tag, "Total de livros inseridos: $mockBooksSize")
        }
    }

    private fun refreshUnreadBooks() {
        viewModelScope.launch {
            try {
                val currentFilter = sessionState.activeFilter

                val unreadBooks = if (currentFilter == null) {
                    saveBooksRepository.getAllUnreadBooks()
                } else {
                    saveBooksRepository.getUnreadBooksByGenre(currentFilter)
                }

                val validShownIds = sessionState.shownIds.filter { id ->
                    unreadBooks.any { it.id == id }
                }.toSet()

                sessionState = sessionState.copy(
                    unreadBooks = unreadBooks,
                    shownIds = validShownIds
                )

                if (unreadBooks.isEmpty()) {
                    _uiState.value = BookUiState.Empty()
                } else {
                    val currentBook = (_uiState.value as? BookUiState.Success)?.book
                    if (currentBook != null && unreadBooks.none { it.id == currentBook.id }) {
                        pickRandomBook()
                    } else {
                        currentBook?.let {
                            val updatedBook = unreadBooks.find { it.id == it.id }
                            if (updatedBook != null && updatedBook != currentBook) {
                                _uiState.value = BookUiState.Success(updatedBook)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "Erro ao atualizar lista de livros", e)
            }
        }
    }

    fun applyFilter(genre: String) {
        if (sessionState.activeFilter == genre) return

        loadBooksAndRandomize(filter = genre)
    }

    fun clearFilter() {
        if (sessionState.activeFilter == null) return

        loadBooksAndRandomize(filter = null)
    }

    fun showFilterSheet() {
        _isFilterSheetVisible.value = true
        loadGenres()
    }

    fun dismissFilterSheet() {
        _isFilterSheetVisible.value = false
    }

    private fun loadGenres() {
        viewModelScope.launch {
            try {
                val genreList = saveBooksRepository.getDistinctGenres()
                _genres.value = genreList
                Log.d(tag, "Gêneros carregados: ${genreList.size}")
            } catch (e: Exception) {
                Log.e(tag, "Erro ao carregar gêneros", e)
                _genres.value = emptyList()
            }
        }
    }
}