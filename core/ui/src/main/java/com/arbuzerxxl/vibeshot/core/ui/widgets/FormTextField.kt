package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.cornerRadius16
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews

@Composable
fun FormTextField(
    value: String,
    onChange: (value: String) -> Unit,
    isError: Boolean = false,
    placeholder: String = "",
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {

    TextField(
        modifier = Modifier
            .padding(vertical = padding8)
            .fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius16),
        value = value,
        onValueChange = {
            if ("\n" !in it) onChange(it)
        },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Start
            )
        },
        textStyle = MaterialTheme.typography.labelMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp
        ),
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        isError = isError
    )
}

@DevicePreviews
@Composable
private fun FormTextFieldPreview() {
    VibeShotThemePreview {
        FormTextField(
            value = "",
            singleLine = false,
            isError = false,
            placeholder = "Tittle",
            onChange = {},
        )
    }
}