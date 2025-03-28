package com.arbuzerxxl.vibeshot.features.auth_impl.presentation


import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import org.koin.android.compat.ScopeCompat.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


@Composable
fun AuthScreen(
    uiState: AuthUiState,
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
    getAuthorizeUrl: (String) -> String,
    onSignIn: (String, String) -> Unit
) {

    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(onClick = onStart) {
                Text(text = "Start")
            }
            when (uiState) {
                is AuthUiState.Loading -> {
                    Text(text = "Token: ----")
                }

                is AuthUiState.RequestTokenSuccess -> {
                    Text(text = "Token: ${uiState.requestToken}")
                    openTab(context = context, url = getAuthorizeUrl(uiState.requestToken))
                }

                is AuthUiState.VerifierSuccess -> {
                    Text(text = "Verifier: ${uiState.verifier}")
                    onSignIn(uiState.requestToken, uiState.verifier)
                }

                is AuthUiState.AccessTokenSuccess -> {
                    Text(text = "Access: ${uiState.accessToken}")
                }
                is AuthUiState.Success -> Unit
            }

        }
    }
}

// TODO openTab works when chrome installed only
private fun openTab(context: Context, url: String) {

    val builder = CustomTabsIntent.Builder()
    builder.setShowTitle(true)
    builder.setInstantAppsEnabled(true)

    val customBuilder = builder.build()

    customBuilder.intent.setPackage("com.android.chrome")
    customBuilder.launchUrl(context, Uri.parse(url))

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AuthScreenPreview() {
    VibeShotTheme {
        AuthScreen(
            uiState = AuthUiState.Loading,
            onStart = {},
            getAuthorizeUrl = { value: String -> value},
            onSignIn = { _: String, _: String -> Unit}
        )
    }
}
