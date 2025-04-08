package com.arbuzerxxl.vibeshot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.navigation.VibeShotHost
import com.google.samples.apps.nowinandroid.util.isSystemInDarkTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val authRepository: AuthRepository by inject()
    private val viewModel by inject<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var themeSettings by mutableStateOf(
            ThemeSettings(
                darkTheme = resources.configuration.isSystemInDarkTheme,
            ),
        )

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                combine(
                    isSystemInDarkTheme(),
                    viewModel.uiState,
                ) { systemDark, uiState ->
                    ThemeSettings(uiState.shouldUseDarkTheme(systemDark))
                }
                    .onEach { settings ->
                        themeSettings = settings
                    }
                    .collect { }
            }
        }

        splashscreen.setKeepOnScreenCondition {
            viewModel.keepSplashScreen.value
        }
        handleFlickrAuthIntent(intent)
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            if (!uiState.shouldKeepSplashScreen()) viewModel.disableSplashScreen()
            VibeShotTheme(
                darkTheme = themeSettings.darkTheme
            ) {
                VibeShotApp(uiState.shouldSkipAuth())
            }
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("DEEP_LINK", "Received URI: ${intent.data}")
        handleFlickrAuthIntent(intent)
    }

    private fun handleFlickrAuthIntent(intent: Intent) {
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
fun VibeShotApp(skipAuth: Boolean) {
    VibeShotHost(skipAuth = skipAuth)
}

/**
 * Class for the system theme settings.
 * This wrapping class allows us to combine all the changes and prevent unnecessary recompositions.
 */
data class ThemeSettings(
    val darkTheme: Boolean,
)
