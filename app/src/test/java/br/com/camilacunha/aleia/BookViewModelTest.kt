package br.com.camilacunha.aleia

import br.com.camilacunha.aleia.DataFactoryTest.book
import br.com.camilacunha.aleia.domain.BookRemoteRepository
import br.com.camilacunha.aleia.domain.SaveBooksRepository
import br.com.camilacunha.aleia.ui.BookViewModel
import br.com.camilacunha.aleia.ui.state.BookUiState
import br.com.camilacunha.aleia.ui.state.EmptyAction
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val saveBooksRepository: SaveBooksRepository = mockk()
    private val remoteRepository: BookRemoteRepository = mockk()

    @Before
    fun setup() {
        coEvery { saveBooksRepository.getBooksMissingMetadata() } returns emptyList()
    }

    @Test
    fun `when init and repository has books, then uiState is Success`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val books = listOf(
                book(id = 1, title = "O Hobbit"),
                book(id = 2, title = "Duna")
            )
            coEvery { saveBooksRepository.getAllUnreadBooks() } returns books

            val viewModel = BookViewModel(saveBooksRepository, remoteRepository)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue("Esperado Success, foi $state", state is BookUiState.Success)
            val shownBook = (state as BookUiState.Success).book
            assertTrue(
                "Livro mostrado deve ser um dos dois (id 1 ou 2), foi ${shownBook.id}",
                shownBook.id == 1 || shownBook.id == 2
            )
        }

    @Test
    fun `when randomizeAgain is called with two books, then shows the other book`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val hobbit = book(id = 1, title = "O Hobbit")
            val duna = book(id = 2, title = "Duna")
            coEvery { saveBooksRepository.getAllUnreadBooks() } returns listOf(hobbit, duna)

            val viewModel = BookViewModel(saveBooksRepository, remoteRepository)
            advanceUntilIdle()

            val firstState = viewModel.uiState.value as BookUiState.Success
            val firstId = firstState.book.id

            viewModel.randomizeAgain()
            advanceUntilIdle()

            val secondState = viewModel.uiState.value
            assertTrue("Esperado Success, foi $secondState", secondState is BookUiState.Success)
            val secondId = (secondState as BookUiState.Success).book.id
            assertTrue(
                "Esperado o livro diferente do primeiro ($firstId), foi $secondId",
                secondId != firstId
            )
        }

    @Test
    fun `when randomizeAgain is called and all books were shown, then uiState is Empty`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val hobbit = book(id = 1, title = "O Hobbit")
            coEvery { saveBooksRepository.getAllUnreadBooks() } returns listOf(hobbit)

            val viewModel = BookViewModel(saveBooksRepository, remoteRepository)
            advanceUntilIdle()

            viewModel.randomizeAgain()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue("Esperado Empty, foi $state", state is BookUiState.Empty)
        }

    @Test
    fun `when acceptSuggestion is called, then markAsRead is called and state advances`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val hobbit = book(id = 1, title = "O Hobbit")
            val duna = book(id = 2, title = "Duna")
            coEvery { saveBooksRepository.getAllUnreadBooks() } returns listOf(hobbit, duna)
            coEvery { saveBooksRepository.markAsRead(any()) } returns Unit

            val viewModel = BookViewModel(saveBooksRepository, remoteRepository)
            advanceUntilIdle()

            val currentBook = (viewModel.uiState.value as BookUiState.Success).book

            viewModel.acceptSuggestion()
            advanceUntilIdle()

            coVerify(exactly = 1) { saveBooksRepository.markAsRead(currentBook.id) }

            val state = viewModel.uiState.value
            assertTrue("Esperado Success, foi $state", state is BookUiState.Success)
            val shownBook = (state as BookUiState.Success).book
            assertTrue(
                "Esperado o livro diferente de ${currentBook.id}, foi ${shownBook.id}",
                shownBook.id != currentBook.id
            )
        }

    @Test
    fun `when acceptSuggestion is called but state is not Success, then does nothing`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery { saveBooksRepository.getAllUnreadBooks() } returns emptyList()

            val viewModel = BookViewModel(saveBooksRepository, remoteRepository)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value is BookUiState.Empty)

            viewModel.acceptSuggestion()
            advanceUntilIdle()

            coVerify(exactly = 0) { saveBooksRepository.markAsRead(any()) }
        }

    @Test
    fun `when applyFilter is called with a new genre, then repository is queried and state is Success`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val hobbit = book(id = 1, title = "O Hobbit", genre = "Fantasia")
            val duna = book(id = 2, title = "Duna", genre = "Ficção Científica")
            coEvery { saveBooksRepository.getAllUnreadBooks() } returns listOf(hobbit, duna)
            coEvery { saveBooksRepository.getUnreadBooksByGenre("Fantasia") } returns listOf(hobbit)

            val viewModel = BookViewModel(saveBooksRepository, remoteRepository)
            advanceUntilIdle()

            viewModel.applyFilter("Fantasia")
            advanceUntilIdle()

            coVerify(exactly = 1) { saveBooksRepository.getUnreadBooksByGenre("Fantasia") }
            val state = viewModel.uiState.value
            assertTrue("Esperado Success, foi $state", state is BookUiState.Success)
            assertEquals("O Hobbit", (state as BookUiState.Success).book.title)
        }

    @Test
    fun `when applyFilter is called with the current genre, then repository is not queried`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val hobbit = book(id = 1, title = "O Hobbit", genre = "Fantasia")
            val duna = book(id = 2, title = "Duna", genre = "Fantasia")
            coEvery { saveBooksRepository.getAllUnreadBooks() } returns listOf(hobbit, duna)
            coEvery { saveBooksRepository.getUnreadBooksByGenre("Fantasia") } returns listOf(
                hobbit,
                duna
            )

            val viewModel = BookViewModel(saveBooksRepository, remoteRepository)
            advanceUntilIdle()

            viewModel.applyFilter("Fantasia")
            advanceUntilIdle()

            viewModel.applyFilter("Fantasia")
            advanceUntilIdle()

            coVerify(exactly = 1) { saveBooksRepository.getUnreadBooksByGenre("Fantasia") }
        }

    @Test
    fun `when database has no unread books, then Empty has ADD_BOOK action`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery { saveBooksRepository.getAllUnreadBooks() } returns emptyList()

            val viewModel = BookViewModel(saveBooksRepository, remoteRepository)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue("Esperado Empty, foi $state", state is BookUiState.Empty)
            assertEquals(EmptyAction.ADD_BOOK, (state as BookUiState.Empty).action)
        }

    @Test
    fun `when all books are shown in session, then Empty has RESTART_SESSION action`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val hobbit = book(id = 1, title = "O Hobbit")
            coEvery { saveBooksRepository.getAllUnreadBooks() } returns listOf(hobbit)

            val viewModel = BookViewModel(saveBooksRepository, remoteRepository)
            advanceUntilIdle()

            viewModel.randomizeAgain()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue("Esperado Empty, foi $state", state is BookUiState.Empty)
            assertEquals(EmptyAction.RESTART_SESSION, (state as BookUiState.Empty).action)
        }

    @Test
    fun `when restartSession is called, then all books can be shown again`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val hobbit = book(id = 1, title = "O Hobbit")
            coEvery { saveBooksRepository.getAllUnreadBooks() } returns listOf(hobbit)

            val viewModel = BookViewModel(saveBooksRepository, remoteRepository)
            advanceUntilIdle()

            viewModel.randomizeAgain()
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value is BookUiState.Empty)

            viewModel.restartSession()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue("Esperado Success, foi $state", state is BookUiState.Success)
            assertEquals("O Hobbit", (state as BookUiState.Success).book.title)
        }
}