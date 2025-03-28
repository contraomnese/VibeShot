package com.arbuzerxxl.vibeshot

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.features.auth_impl.presentation.AuthScreen
import com.arbuzerxxl.vibeshot.features.auth_impl.presentation.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        setContent {
            VibeShotApp(
                viewModel = authViewModel
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data = intent.data
        if (data != null && data.toString().startsWith("flickr-auth://oauth")) {
            val oauthVerifier = data.getQueryParameter("oauth_verifier")
            val oauthToken = data.getQueryParameter("oauth_token")
            if (oauthVerifier != null && oauthToken != null) {
                authViewModel.onVerifierChange(verifier = oauthVerifier, token = oauthToken)
            }
        }
    }
}

@Composable
fun VibeShotApp(viewModel: AuthViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VibeShotTheme {
        AuthScreen(
            uiState = uiState,
            onStart = viewModel::onStart,
            getAuthorizeUrl = viewModel::getAuthorizeUrl,
            onSignIn = viewModel::getAccessToken
        )
    }
}
