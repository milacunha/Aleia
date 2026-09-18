package br.com.camilacunha.aleia.ui.screen

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.camilacunha.aleia.R
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import br.com.camilacunha.aleia.ui.theme.Background
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.drawable.MovieDrawable.Companion.REPEAT_INFINITE
import coil.request.ImageRequest
import coil.request.repeatCount

//TODO("trocar o gif de loading")
@Composable
fun LoadingScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.raw.loading)
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
            contentDescription = stringResource(R.string.loading),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(240.dp)
        )

        Text(
            text = stringResource(R.string.loading).uppercase(),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(all = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    AleiaTheme {
        LoadingScreen()
    }
}