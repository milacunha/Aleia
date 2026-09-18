package br.com.camilacunha.aleia.ui.screen

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.camilacunha.aleia.ui.component.NeoBrutButton
import br.com.camilacunha.aleia.ui.state.EmptyState
import br.com.camilacunha.aleia.ui.state.ErrorState
import br.com.camilacunha.aleia.ui.state.LoadingState
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import br.com.camilacunha.aleia.ui.theme.Background
import br.com.camilacunha.aleia.ui.theme.Black
import br.com.camilacunha.aleia.ui.theme.PurpleNeo
import br.com.camilacunha.aleia.ui.theme.White
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.drawable.MovieDrawable.Companion.REPEAT_INFINITE
import coil.request.ImageRequest
import coil.request.repeatCount

@Composable
fun MessageScreen(
    gifRes: Int,
    title: String,
    message: String? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(bottom = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(gifRes)
                    .decoderFactory(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            ImageDecoderDecoder.Factory()
                        } else {
                            GifDecoder.Factory()
                        }
                    )
                    .repeatCount(REPEAT_INFINITE)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(240.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )

            message?.let {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = it.uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        onAction?.let {
            NeoBrutButton(
                onClick = it,
                modifier = Modifier
                    .size(300.dp, 40.dp)
                    .align(Alignment.BottomCenter),
                text = actionText?.uppercase() ?: "Tente novamente".uppercase(),
                backgroundColor = PurpleNeo,
                style = MaterialTheme.typography.displayMedium,
                shadowColor = Black,
                shadowBorderColor = Black,
                textColor = White
            )
        }
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    AleiaTheme {
        LoadingState()
    }
}

@Preview
@Composable
private fun EmptyScreenPreview() {
    AleiaTheme {
        EmptyState(
            message = "Nenhum livro disponível",
            actionText = "Adicionar livro",
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    AleiaTheme {
        ErrorState(
            message = "Mensagem bem grande de algum erro generico na api ou no banco de dados",
            onRetry = {}
        )
    }
}