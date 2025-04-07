package com.arbuzerxxl.vibeshot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.navigation.VibeShotHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val authRepository: AuthRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        var keepSplashScreen = true
        super.onCreate(savedInstanceState)
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch {
            delay(5000)
            keepSplashScreen = false
        } // mock
        handleIntent(intent)
        setContent {
            VibeShotApp()
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("DEEP_LINK", "Received URI: ${intent.data}")
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data = intent.data
        if (data != null && data.toString().startsWith(BuildConfig.FLICKR_API_CALLBACK)) {
            val oauthVerifier = data.getQueryParameter("oauth_verifier")
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
