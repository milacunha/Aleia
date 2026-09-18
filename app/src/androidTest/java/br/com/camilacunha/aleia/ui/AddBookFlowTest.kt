package br.com.camilacunha.aleia.ui

import android.content.Context
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import br.com.camilacunha.aleia.BaseBookScreenTest
import br.com.camilacunha.aleia.DataFactoryTest.entity
import br.com.camilacunha.aleia.R
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

class AddBookFlowTest : BaseBookScreenTest() {

    @Test
    fun whenUserAddsBookWithoutMetadata_thenBookIsPersisted() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val searchBookText = context.getString(R.string.search_book).uppercase()
        val addWithoutMetadataText = context.getString(R.string.not_found_add_book).uppercase()
        val successText = context.getString(R.string.found_book).uppercase()

        runBlocking { dao.insertBook(entity(title = "Livro Existente")) }

        renderScreen()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Livro Existente", ignoreCase = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("+").performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(
                text = searchBookText,
                ignoreCase = true,
                useUnmergedTree = true
            ).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNode(hasSetTextAction(), useUnmergedTree = true)
            .performTextInput("O Hobbit")

        composeRule.onNodeWithText(
            text = searchBookText,
            ignoreCase = true,
            useUnmergedTree = true
        ).performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(
                text = addWithoutMetadataText,
                ignoreCase = true,
                useUnmergedTree = true
            ).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText(
            text = addWithoutMetadataText,
            ignoreCase = true,
            useUnmergedTree = true
        ).performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(
                text = successText,
                ignoreCase = true,
                useUnmergedTree = true
            ).fetchSemanticsNodes().isNotEmpty()
        }

        val saved = runBlocking {
            dao.getAllUnreadBooks().firstOrNull { it.title == "O Hobbit" }
        }
        assertNotNull("Livro deveria estar no banco após o fluxo", saved)
    }
}