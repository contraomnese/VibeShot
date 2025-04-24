package com.arbuzerxxl.vibeshot.core.design.theme


import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.arbuzerxxl.vibeshot.core.design.R

private val montserrat = GoogleFont("Montserrat")
private val openSans = GoogleFont("Open Sans")

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val vibeTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(googleFont=montserrat, fontProvider = provider)),
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(googleFont=montserrat, fontProvider = provider)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(googleFont=openSans, fontProvider = provider)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(googleFont=montserrat, fontProvider = provider)),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)