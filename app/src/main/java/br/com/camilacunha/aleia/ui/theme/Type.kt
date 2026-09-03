package br.com.camilacunha.aleia.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.camilacunha.aleia.R

val Typography = Typography(

    //titles
    titleLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = FontFamily(Font(R.font.space_grotesk)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 3.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.space_grotesk)),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        letterSpacing = 2.sp
    ),

    //cover
    headlineLarge = TextStyle(
        fontSize = 28.sp,
        fontFamily = FontFamily(Font(R.font.space_grotesk)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.space_grotesk)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 2.sp
    ),

    //texts
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.space_grotesk)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 2.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.space_grotesk)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 2.sp
    ),

    //buttons
    displayLarge = TextStyle(
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.space_grotesk)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
    ),
    displayMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.space_grotesk)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
    ),
    displaySmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.space_grotesk)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp
    ),
)