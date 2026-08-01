package br.com.camilacunha.aleia.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.camilacunha.aleia.domain.SaveBooksRepository
import br.com.camilacunha.aleia.domain.model.AddBookResult
import br.com.camilacunha.aleia.domain.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val saveBooksRepository: SaveBooksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookUiState>(BookUiState.Loading)
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    private var sessionState = SessionState()

    init {
        loadBooksAndRandomize()
    }

    private fun loadBooksAndRandomize() {
        viewModelScope.launch {
            _uiState.value = BookUiState.Loading
            try {
                val unreadBooks = saveBooksRepository.getAllUnreadBooks()
                sessionState = SessionState(unreadBooks = unreadBooks)
                Log.d("BookViewModel", "livros nao lidos: $unreadBooks")
                if (unreadBooks.isEmpty()) {
                    _uiState.value = BookUiState.Empty()
                } else {
                    pickRandomBook()
                }
            } catch (e: Exception) {
                Log.e("BookViewModel", "Erro ao carregar livros: ", e)
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
                Log.e("BookViewModel", "Sugestão aceita: $currentBook")
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
                Log.e("BookViewModel", "Erro ao aceitar sugestão: ", e)
                _uiState.value = BookUiState.Error("Erro ao aceitar sugestão: ${e.message}")
            }
        }
    }

    private fun pickRandomBook() {
        val available = sessionState.unreadBooks.filter { it.id !in sessionState.shownIds }
        Log.d("BookViewModel", "Livros disponíveis: $available")
        if (available.isEmpty()) {
            _uiState.value = BookUiState.Empty("Todos os livros já foram mostrados nesta sessão")
        } else {
            val randomBook = available.random()
            Log.d("BookViewModel", "Livro sorteado: $randomBook")
            sessionState = sessionState.copy(
                shownIds = sessionState.shownIds + randomBook.id
            )
            _uiState.value = BookUiState.Success(randomBook)
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
                            Log.d("BookViewModel", "Livro adicionado com sucesso!")
                        }

                        AddBookResult.AlreadyExists -> {
                            Log.d("BookViewModel", "Livro já existe no banco!")
                        }

                        is AddBookResult.Error -> {
                            Log.d("BookViewModel", "Erro genérico: ${result.message}")
                        }
                    }
                }
            }

            val mockBooksSize = saveBooksRepository.getAllUnreadBooks().size
            Log.d("BookViewModel", "Total de livros inseridos: $mockBooksSize")
        }
    }
}