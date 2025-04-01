package com.arbuzerxxl.vibeshot

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.features.auth.presentation.AuthViewModel
import com.arbuzerxxl.vibeshot.navigation.VibeShotHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModel()
    private val authRepository: AuthRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        setContent {
            VibeShotApp()
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

            authViewModel.setLoadingState()
            oauthVerifier?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    authRepository.signIn(it)
                }
            }
        }
    }
}

@Composable
fun VibeShotApp() {
    VibeShotTheme {
        VibeShotHost()
    }
}
