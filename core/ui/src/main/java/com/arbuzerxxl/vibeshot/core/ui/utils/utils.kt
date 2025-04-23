package com.arbuzerxxl.vibeshot.core.ui.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

fun pxToDp(context: Context, px: Int): Float {
    return px / context.resources.displayMetrics.density
}

val Dp.px: Float @Composable get() = with(LocalDensity.current) { this@px.toPx() }