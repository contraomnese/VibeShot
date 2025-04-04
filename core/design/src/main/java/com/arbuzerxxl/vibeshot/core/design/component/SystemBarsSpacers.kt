package com.arbuzerxxl.vibeshot.core.design.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StatusBarInsetsSpacer() {
    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
}

@Composable
fun BottomBarInsetsSpacer() {
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
}