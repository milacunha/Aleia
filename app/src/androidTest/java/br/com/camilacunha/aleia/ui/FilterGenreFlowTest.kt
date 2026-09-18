package br.com.camilacunha.aleia.ui

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import br.com.camilacunha.aleia.BaseBookScreenTest
import br.com.camilacunha.aleia.DataFactoryTest.entity
import br.com.camilacunha.aleia.R
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FilterGenreFlowTest : BaseBookScreenTest() {

    @Test
    fun whenUserFiltersByGenre_thenOnlyBooksOfThatGenreAreShown() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val filterButtonText = context.getString(R.string.filter_genre).uppercase()
        val fantasyGenre = "Fantasia"
        val sciFiGenre = "Ficção Científica"

        runBlocking {
            dao.insertBook(entity(title = "O Hobbit", genre = fantasyGenre))
            dao.insertBook(entity(title = "Duna", genre = sciFiGenre))
        }

        renderScreen()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            val hobbit = composeRule
                .onAllNodesWithText("O Hobbit", ignoreCase = true, useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
            val duna = composeRule
                .onAllNodesWithText("Duna", ignoreCase = true, useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
            hobbit || duna
        }

        composeRule.onNodeWithText(
            text = filterButtonText,
            ignoreCase = true,
            useUnmergedTree = true
        ).performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodes(
                hasText(fantasyGenre, ignoreCase = true) and hasClickAction()
            ).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNode(
            hasText(fantasyGenre, ignoreCase = true) and hasClickAction()
        ).performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule
                .onAllNodesWithText("O Hobbit", ignoreCase = true, useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule
            .onAllNodesWithText("Duna", ignoreCase = true, useUnmergedTree = true)
            .assertCountEquals(0)
    }
}