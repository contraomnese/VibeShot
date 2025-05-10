package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight40
import com.arbuzerxxl.vibeshot.core.design.theme.zero
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.ui.R

@Composable
fun LogIn(
    modifier: Modifier = Modifier,
    onLogInClick: () -> Unit,
    onSkipNowClick: () -> Unit,
) {
    Row(
        modifier = modifier.height(itemHeight40).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.have_account),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ))
            TextButton(
                onClick = onLogInClick,
                contentPadding = PaddingValues(zero)
            ) {
                Text(
                    text = stringResource(R.string.log_in_button),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        VerticalDivider()
        TextButton(
            onClick = onSkipNowClick,
            contentPadding = PaddingValues(zero)
        ) {
            Text(
                text = stringResource(R.string.skip_now_button),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@DevicePreviews
@Composable
private fun LogInPreview() {
    VibeShotThemePreview {
        LogIn(
            onLogInClick = {},
            onSkipNowClick = {}
        )
    }
}