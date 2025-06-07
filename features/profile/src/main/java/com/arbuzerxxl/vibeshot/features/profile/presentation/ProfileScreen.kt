package com.arbuzerxxl.vibeshot.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.core.design.theme.padding24
import com.arbuzerxxl.vibeshot.core.design.theme.padding40
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.core.ui.widgets.BaseButton
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.ui.R
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewmodel: ProfileViewModel = koinViewModel(),
    onLogOutClicked: () -> Unit
) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        modifier = modifier,
        uiState = uiState,
        onLogOutClicked = onLogOutClicked,
        logOut = viewmodel::logout
    )
}

@Composable
internal fun ProfileScreen(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
    onLogOutClicked: () -> Unit,
    logOut: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> LoadingIndicator()
            else -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Hello, ${uiState.username}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

            }
        }
        BaseButton(
            title = stringResource(id = R.string.logout_title_button),
            modifier = Modifier
                .padding(bottom = padding40, start = padding24, end = padding24)
                .align(Alignment.BottomCenter),
            onClicked = { logOut(); onLogOutClicked() }
        )
    }
}

@DevicePreviews
@Composable
fun ProfileScreenPreview() {
    VibeShotTheme {
        ProfileScreen(
            uiState = ProfileUiState(username = "Sergey Belov"),
            onLogOutClicked = {},
            logOut = {}
        )
    }
}
