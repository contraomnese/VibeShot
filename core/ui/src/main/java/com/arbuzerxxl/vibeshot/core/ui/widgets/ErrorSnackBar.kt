package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize8
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight24
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight48
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews

@Composable
fun ErrorSnackBar(
    modifier: Modifier = Modifier,
    message: String,
) {

    Snackbar(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        shape = RoundedCornerShape(cornerSize8),
        modifier = modifier
            .height(itemHeight48)
            .alpha(0.8f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = VibeShotIcons.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.size(itemHeight24)
            )
            Spacer(modifier = Modifier.width(padding16))
            Text(
                modifier = Modifier.wrapContentWidth(),
                text = message,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@DevicePreviews
@Composable
fun ErrorSnackbarPreview() {
    VibeShotThemePreview {
        ErrorSnackBar(
            message = "Message error",
        )
    }
}

@DevicePreviews
@Composable
fun ErrorSnackbarWithLongErrorPreview() {
    VibeShotThemePreview {
        ErrorSnackBar(
            message = "Message error long long long long long long",
        )
    }
}