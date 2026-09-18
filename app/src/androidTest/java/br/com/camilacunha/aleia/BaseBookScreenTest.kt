package br.com.camilacunha.aleia

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import br.com.camilacunha.aleia.data.local.database.AppDatabase
import br.com.camilacunha.aleia.data.local.database.BooksDao
import br.com.camilacunha.aleia.di.createInMemoryDatabase
import br.com.camilacunha.aleia.di.testModule
import br.com.camilacunha.aleia.domain.BookRemoteRepository
import br.com.camilacunha.aleia.ui.BookScreen
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

abstract class BaseBookScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var database: AppDatabase
    lateinit var dao: BooksDao
    private lateinit var remoteRepository: BookRemoteRepository

    @Before
    fun setup() {
        stopKoin()

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = createInMemoryDatabase(context)
        dao = database.bookDao()
        remoteRepository = mockk(relaxed = true)

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(context)
            modules(testModule(dao, remoteRepository))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        database.close()
    }

    protected fun renderScreen() {
        composeRule.setContent {
            AleiaTheme {
                BookScreen()
            }
        }
    }
}