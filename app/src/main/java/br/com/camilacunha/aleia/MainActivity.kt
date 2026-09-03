package br.com.camilacunha.aleia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import br.com.camilacunha.aleia.ui.BookScreen
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import br.com.camilacunha.aleia.ui.theme.Background

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Background.toArgb(),
                darkScrim = Background.toArgb()
            )
        )

        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            true

        setContent {
            AleiaTheme {
                BookScreen()
            }
        }
    }
}