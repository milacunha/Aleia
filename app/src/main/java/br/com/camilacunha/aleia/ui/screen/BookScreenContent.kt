package br.com.camilacunha.aleia.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import br.com.camilacunha.aleia.ui.theme.AquaNeo
import br.com.camilacunha.aleia.ui.theme.Background
import br.com.camilacunha.aleia.ui.theme.Black
import br.com.camilacunha.aleia.ui.theme.DarkGray
import br.com.camilacunha.aleia.ui.theme.LightGray
import br.com.camilacunha.aleia.ui.theme.White
import br.com.camilacunha.aleia.ui.theme.YellowNeo

@Composable
fun BookScreenContent(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
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
                onClick = { },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = "Refresh"
                )
            }
        }

        Box(
            modifier = Modifier
                .size(280.dp, 480.dp)
                .background(White)
                .border(
                    width = 3.dp,
                    color = Black,
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp, 280.dp)
                        .background(LightGray)
                        .border(
                            width = 3.dp,
                            color = Black,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CAPA DO LIVRO".uppercase(),
                        fontSize = 12.sp,
                        color = DarkGray,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "O NOME DO VENTO".uppercase(),
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "FANTASIA".uppercase(),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = YellowNeo,
                contentColor = Black
            ),
            border = BorderStroke(
                width = 3.dp,
                color = Black
            )
        ) {
            Text(
                text = stringResource(R.string.suggestion_accepted).uppercase(),
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.space_grotesk)),
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { },
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AquaNeo,
                    contentColor = Black
                ),
                border = BorderStroke(
                    width = 3.dp,
                    color = Black
                )
            ) {
                Text(
                    text = stringResource(R.string.filter_genre).uppercase(),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Button(
                onClick = { },
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AquaNeo,
                    contentColor = Black
                ),
                border = BorderStroke(
                    width = 3.dp,
                    color = Black
                )
            ) {
                Text(
                    text = "+", fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBookSelectionScreen() {
    MaterialTheme {
        BookScreenContent(
            onClick = {}
        )
    }
}