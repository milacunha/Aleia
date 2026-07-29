package br.com.camilacunha.aleia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.camilacunha.aleia.ui.BookScreen
import br.com.camilacunha.aleia.ui.theme.AleiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AleiaTheme {
                BookScreen()
            }
        }
    }
}