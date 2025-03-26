package com.arbuzerxxl.vibeshot.features.auth_impl.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.domain.models.RequestTokenDomain


@Composable
fun AuthScreen(
    uiState: AuthUiState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(onClick = onClick) {
                Text(text = "Start")
            }
            when (uiState) {
                is AuthUiState.Loading -> {
                    Text(
                        text = "Token: ----"
                    )
                    Text(
                        text = "Secret: ----"
                    )
                }

                is AuthUiState.Success -> {
                    Text(
                        text = "Token: ${uiState.requestToken.requestToken}"
                    )
                    Text(
                        text = "Secret: ${uiState.requestToken.requestTokenSecret}"
                    )
                }
            }

        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AuthScreenPreview() {
    VibeShotTheme {
        AuthScreen(
            uiState = AuthUiState.Success(
                requestToken = RequestTokenDomain(requestToken = "1234", requestTokenSecret = "secret123")
            ),
            onClick = {})
    }
}
