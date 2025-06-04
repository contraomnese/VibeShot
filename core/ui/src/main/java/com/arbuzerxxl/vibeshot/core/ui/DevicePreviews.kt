package com.arbuzerxxl.vibeshot.core.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Preview(name = "phone", showBackground = true, apiLevel = 34, backgroundColor = 0xFFFDFDFD)
@Preview(name = "darkPhone",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF121212, apiLevel = 34)
annotation class DevicePreviews
