package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

@Composable
fun TypingText(
    text: String,
    typingSpeed: Long = 50L,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current
) {
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        visibleText = ""
        for (i in 1..text.length) {
            visibleText = text.take(i)
            delay(typingSpeed)
        }
    }

    Text(
        text = visibleText,
        style = textStyle,
        modifier = modifier
    )
}