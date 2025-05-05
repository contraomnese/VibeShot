package com.arbuzerxxl.vibeshot.core.design.theme


import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.arbuzerxxl.vibeshot.core.design.R
import androidx.compose.ui.text.font.Font as AndrFont

private val montserrat = GoogleFont("Montserrat")
private val openSans = GoogleFont("Open Sans")
private val anton = GoogleFont("Anton")
private val ramabhadra = GoogleFont("Ramabhadra")

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val vibeTypography = Typography(
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(googleFont=montserrat, fontProvider = provider)),
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(googleFont=ramabhadra, fontProvider = provider)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(googleFont=anton, fontProvider = provider)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(googleFont=ramabhadra, fontProvider = provider)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(googleFont=montserrat, fontProvider = provider)),
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
)


@OptIn(ExperimentalTextApi::class)
val vibeTypographyPreview = Typography(
    displayMedium = TextStyle(
        fontFamily = FontFamily(AndrFont(R.font.montserrat_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily(AndrFont(R.font.ramabhadra_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(AndrFont(R.font.anton_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(AndrFont(R.font.ramabhadra_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(AndrFont(R.font.montserrat_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
)