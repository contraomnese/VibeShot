package com.arbuzerxxl.vibeshot.core.design.theme

import com.arbuzerxxl.vibeshot.core.design.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val victorMonoFamily = FontFamily(
    Font(resId = R.font.victor_mono_bold, weight = FontWeight.Bold),
    Font(resId = R.font.victor_mono_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(resId = R.font.victor_mono_extra_light, weight = FontWeight.ExtraLight),
    Font(resId = R.font.victor_mono_extra_light_italic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),
    Font(resId = R.font.victor_mono_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(resId = R.font.victor_mono_light, weight = FontWeight.Light),
    Font(resId = R.font.victor_mono_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(resId = R.font.victor_mono_medium, weight = FontWeight.Medium),
    Font(resId = R.font.victor_mono_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(resId = R.font.victor_mono_regular, weight = FontWeight.Normal),
    Font(resId = R.font.victor_mono_semi_bold, weight = FontWeight.SemiBold),
    Font(resId = R.font.victor_mono_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
    Font(resId = R.font.victor_mono_thin, weight = FontWeight.Thin),
    Font(resId = R.font.victor_mono_thin_italic, weight = FontWeight.Thin, style = FontStyle.Italic),
)

private val dancingScriptFamily = FontFamily(
    Font(resId = R.font.dancing_script_semi_bold, weight = FontWeight.SemiBold)
)


val vibeTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 36.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = dancingScriptFamily
    ),
    headlineMedium = TextStyle(
        fontSize = 32.sp,
        lineHeight = 32.sp,
        fontFamily = victorMonoFamily,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic
    ),
    titleMedium = TextStyle(
        fontSize = 24.sp,
        fontFamily = victorMonoFamily,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic
    ),
    titleSmall = TextStyle(
        fontSize = 17.sp,
        fontFamily = victorMonoFamily,
        fontWeight = FontWeight.W600,
    ),
    labelLarge = TextStyle(
        fontSize = 20.sp,
        fontFamily = victorMonoFamily,
        fontWeight = FontWeight.Bold
    ),
    labelMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 17.36.sp,
        fontFamily = victorMonoFamily,
        fontWeight = FontWeight.SemiBold
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp,
        fontFamily = victorMonoFamily,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = TextStyle(
        fontSize = 10.sp,
        fontFamily = victorMonoFamily,
        fontWeight = FontWeight.Normal
    )

)