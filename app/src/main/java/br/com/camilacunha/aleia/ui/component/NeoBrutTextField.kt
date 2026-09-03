package br.com.camilacunha.aleia.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import br.com.camilacunha.aleia.ui.theme.AleiaTheme
import br.com.camilacunha.aleia.ui.theme.Background
import br.com.camilacunha.aleia.ui.theme.Black
import br.com.camilacunha.aleia.ui.theme.DarkGray
import br.com.camilacunha.aleia.ui.theme.PurpleNeo

@Composable
fun NeoBrutTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color = Background,
    borderColor: Color = Black,
    textColor: Color = Black,
    shadowColor: Color = PurpleNeo,
    borderWidth: Dp = 3.dp,
    shadowOffset: DpOffset = DpOffset(6.dp, 6.dp),
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(shadowOffset.x, shadowOffset.y)
                .background(shadowColor)
        )

        OutlinedTextField(
            value = value.uppercase(),
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .border(borderWidth, borderColor),
            label = {
                Text(
                    text = label.uppercase(),
                    modifier = Modifier.offset(y = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkGray
                )
            },
            singleLine = singleLine,
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = textColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = textColor,
                focusedLabelColor = DarkGray,
                unfocusedLabelColor = DarkGray,
                disabledLabelColor = DarkGray
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NeoBrutTextFieldPreview() {
    AleiaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NeoBrutTextField(
                value = "",
                onValueChange = { },
                label = "Label"
            )

            Spacer(modifier = Modifier.height(100.dp))

            NeoBrutTextField(
                value = "Value",
                onValueChange = { },
                label = "Label"
            )
        }
    }
}