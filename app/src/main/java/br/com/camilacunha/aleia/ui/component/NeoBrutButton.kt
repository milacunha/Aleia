package br.com.camilacunha.aleia.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import br.com.camilacunha.aleia.ui.theme.Black
import br.com.camilacunha.aleia.ui.theme.PurpleNeo
import br.com.camilacunha.aleia.ui.theme.YellowNeo

@Composable
fun NeoBrutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color,
    borderColor: Color = Black,
    textColor: Color = Black,
    shadowColor: Color = PurpleNeo,
    shadowBorderColor: Color = PurpleNeo,
    borderWidth: Dp = 3.dp,
    shadowOffset: DpOffset = DpOffset(6.dp, 6.dp),
    style: TextStyle
) {
    Box(
        modifier = modifier.clickable(
            onClick = { onClick() }
        ),
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
            Text(
                text = text.uppercase(),
                color = textColor,
                textAlign = TextAlign.Center,
                style = style
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NeoBrutButtonPreview() {
    AleiaTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NeoBrutButton(
                onClick = {},
                modifier = Modifier.size(200.dp, 70.dp),
                text = "Aceitar Sugestão",
                backgroundColor = YellowNeo,
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(100.dp))

            NeoBrutButton(
                onClick = {},
                modifier = Modifier.size(200.dp, 70.dp),
                text = "Aceitar Sugestão",
                backgroundColor = YellowNeo,
                style = MaterialTheme.typography.displayLarge
            )
        }
    }
}