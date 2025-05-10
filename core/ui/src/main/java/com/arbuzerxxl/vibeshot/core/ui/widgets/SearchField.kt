package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.cornerRadius8
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight64
import com.arbuzerxxl.vibeshot.ui.R

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector = VibeShotIcons.Search,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchQuery,
        shape = RoundedCornerShape(cornerRadius8),
        modifier =
            modifier
                .height(itemHeight64),
        placeholder = {
            Text(
                text = stringResource(id = R.string.search),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        visualTransformation = visualTransformation,
        onValueChange = { searchQuery = it },
        leadingIcon = {
            Image(
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp),
                imageVector = leadingIcon,
                contentDescription = "form icon",
                contentScale = ContentScale.None
            )

        },
        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
            onSearch = {
                onValueChange(searchQuery.trim())
                keyboardController?.hide()
            }
        ),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}


@Preview(showBackground = true)
@Composable
fun FormFieldPreview() {
    VibeShotThemePreview {
        SearchField(
            onValueChange = {},
        )
    }
}