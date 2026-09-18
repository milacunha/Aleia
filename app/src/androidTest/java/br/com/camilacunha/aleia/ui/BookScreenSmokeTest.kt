package br.com.camilacunha.aleia.ui

import androidx.compose.ui.test.onAllNodesWithText
import br.com.camilacunha.aleia.BaseBookScreenTest
import br.com.camilacunha.aleia.DataFactoryTest.entity
import kotlinx.coroutines.runBlocking
import org.junit.Test

class BookScreenSmokeTest : BaseBookScreenTest() {

    @Test
    fun whenDatabaseHasUnreadBook_thenTitleIsShown() {
        runBlocking {
            dao.insertBook(entity(title = "O Hobbit"))
        }

        renderScreen()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(
                text = "O Hobbit",
                ignoreCase = true,
                useUnmergedTree = true
            )
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }
}