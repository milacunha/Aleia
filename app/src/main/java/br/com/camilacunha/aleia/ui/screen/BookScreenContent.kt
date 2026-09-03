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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.camilacunha.aleia.R
import br.com.camilacunha.aleia.ui.component.NeoBrutButton
import br.com.camilacunha.aleia.ui.component.NeoBrutCover
import br.com.camilacunha.aleia.ui.state.FilterUiState
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import br.com.camilacunha.aleia.ui.theme.AquaNeo
import br.com.camilacunha.aleia.ui.theme.Background
import br.com.camilacunha.aleia.ui.theme.YellowNeo

@Composable
fun BookScreenContent(
    bookTitle: String,
    bookGenre: String,
    bookCover: String?,
    filterState: FilterUiState,
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
                style = MaterialTheme.typography.titleLarge
            )

            IconButton(
                onClick = onTryAgain,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = stringResource(R.string.refresh)
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
            onClick = onAccepted,
            modifier = Modifier.size(300.dp, 70.dp),
            text = stringResource(R.string.suggestion_accepted).uppercase(),
            backgroundColor = YellowNeo,
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeoBrutButton(
                onClick = onFilter,
                modifier = Modifier.size(100.dp, 70.dp),
                text = when (filterState) {
                    is FilterUiState.Inactive -> stringResource(R.string.filter_genre).uppercase()
                    is FilterUiState.Active -> filterState.genre.uppercase()
                },
                backgroundColor = AquaNeo,
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 14.sp)
            )

            NeoBrutButton(
                onClick = onAddBook,
                modifier = Modifier.size(70.dp, 70.dp),
                text = "+",
                backgroundColor = AquaNeo,
                style = MaterialTheme.typography.displayLarge
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
            onAddBook = {},
            filterState = FilterUiState.Inactive
        )
    }
}