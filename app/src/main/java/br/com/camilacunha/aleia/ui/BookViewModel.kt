package br.com.camilacunha.aleia.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.camilacunha.aleia.domain.SaveBooksRepository
import br.com.camilacunha.aleia.domain.model.AddBookResult
import br.com.camilacunha.aleia.domain.model.Book
import kotlinx.coroutines.launch

class BookViewModel(
    private val saveBooksRepository: SaveBooksRepository
) : ViewModel() {

    fun addMockBooks() {
        viewModelScope.launch {
            val mockBooks = listOf(
                Book(title = "O Hobbit", genre = "Fantasia"),
                Book(title = "1984", genre = "Ficção Científica"),
                Book(title = "Orgulho e Preconceito", genre = "Romance"),
                Book(title = "O Hobbit", genre = "Fantasia")
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

            val mockBooksSize = saveBooksRepository.getAllBooks().size
            Log.d("BookViewModel", "Total de livros inseridos: $mockBooksSize")
        }
    }
}