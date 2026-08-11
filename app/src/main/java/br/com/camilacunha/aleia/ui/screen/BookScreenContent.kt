package br.com.camilacunha.aleia.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.camilacunha.aleia.R
import br.com.camilacunha.aleia.ui.component.NeoBrutButton
import br.com.camilacunha.aleia.ui.component.NeoBrutCover
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import br.com.camilacunha.aleia.ui.theme.AquaNeo
import br.com.camilacunha.aleia.ui.theme.Background
import br.com.camilacunha.aleia.ui.theme.YellowNeo

@Composable
fun BookScreenContent(
    bookTitle: String,
    bookGenre: String,
    bookCover: String?,
    onTryAgain: () -> Unit,
    onAccepted: () -> Unit,
    onFilter: () -> Unit,
    onAddBook: () -> Unit
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(Background)
            .padding(bottom = 16.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.app_name).uppercase(),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.space_grotesk)),
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
            )

            IconButton(
                onClick = onTryAgain,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = "Refresh"
                )
            }
        }

        NeoBrutCover(
            bookTitleText = bookTitle,
            bookGenreText = bookGenre,
            bookCover = bookCover
        )

        Spacer(modifier = Modifier.height(32.dp))

        NeoBrutButton(
            modifier = Modifier.size(300.dp, 70.dp),
            onClick = onAccepted,
            text = stringResource(R.string.suggestion_accepted).uppercase(),
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.space_grotesk)),
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            backgroundColor = YellowNeo
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeoBrutButton(
                modifier = Modifier.size(100.dp, 70.dp),
                onClick = onFilter,
                text = stringResource(R.string.filter_genre).uppercase(),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.space_grotesk)),
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                backgroundColor = AquaNeo
            )

            NeoBrutButton(
                modifier = Modifier.size(70.dp, 70.dp),
                onClick = onAddBook,
                text = "+",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.space_grotesk)),
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                backgroundColor = AquaNeo
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBookSelectionScreen() {
    AleiaTheme {
        BookScreenContent(
            bookTitle = "O nome do vento",
            bookGenre = "fantasia",
            bookCover = null,
            onTryAgain = {},
            onAccepted = {},
            onFilter = {},
            onAddBook = {}
        )
    }
}