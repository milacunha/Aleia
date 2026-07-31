package br.com.camilacunha.aleia.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.camilacunha.aleia.R
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import br.com.camilacunha.aleia.ui.theme.Black
import br.com.camilacunha.aleia.ui.theme.LightGray
import br.com.camilacunha.aleia.ui.theme.PurpleNeo
import br.com.camilacunha.aleia.ui.theme.White

@Composable
fun NeoBrutCover(
    bookTitleText: String,
    bookGenreText: String,
    bookCover: Painter?,
    backgroundColor: Color = White,
    borderColor: Color = Black,
    shadowColor: Color = PurpleNeo,
    shadowBorderColor: Color = PurpleNeo,
    borderWidth: Dp = 3.dp,
    shadowOffset: DpOffset = DpOffset(6.dp, 6.dp)
) {
    Box(
        modifier = Modifier.size(280.dp, 480.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(shadowOffset.x, shadowOffset.y)
                .background(shadowColor)
                .border(borderWidth, shadowBorderColor)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(backgroundColor)
                .border(borderWidth, borderColor),
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
                    Image(
                        modifier = Modifier.size(70.dp),
                        painter = bookCover ?: painterResource(R.drawable.book),
                        contentDescription = "book_cover"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = bookTitleText.uppercase(),
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = bookGenreText.uppercase(),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NeoBrutCoverPreview() {
    AleiaTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NeoBrutCover(
                bookTitleText = "O nome do vento",
                bookGenreText = "fantasia",
                bookCover = painterResource(R.drawable.book),
            )
        }
    }
}