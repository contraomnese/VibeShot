package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.core.design.theme.corner_size_16
import com.arbuzerxxl.vibeshot.core.design.theme.height_base_button
import com.arbuzerxxl.vibeshot.core.design.theme.padding_5
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.ui.R

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    title: String,
    contentColor: Color,
    containerColor: Color,
    onClicked: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(height_base_button),
        onClick = onClicked,
        contentPadding = PaddingValues(vertical = padding_5),
        colors = ButtonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(corner_size_16)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@DevicePreviews
@Composable
private fun BaseButtonPreview() {
    VibeShotTheme {
        BaseButton(
            title = stringResource(id = R.string.get_started_title_button),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            onClicked = { }
        )
    }
}