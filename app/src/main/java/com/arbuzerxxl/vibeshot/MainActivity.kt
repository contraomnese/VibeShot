package com.arbuzerxxl.vibeshot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.domain.usecases.auth.GetRequestTokenUseCase
import com.arbuzerxxl.vibeshot.features.auth_impl.presentation.AuthScreen
import com.arbuzerxxl.vibeshot.features.auth_impl.presentation.AuthViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val getRequestTokenUseCase by inject<GetRequestTokenUseCase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VibeShotApp(getRequestTokenUseCase)
        }
    }
}

@Composable
fun VibeShotApp(useCase: GetRequestTokenUseCase) {

    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(
            getRequestTokenUseCase = useCase
        )
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VibeShotTheme {
        AuthScreen(
            uiState = uiState,
            onClick = viewModel::onStart
        )
    }
}