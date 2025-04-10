package com.arbuzerxxl.vibeshot.features.interests.presentation

import android.R.attr.description
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.features.interests.di.interestsModule
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoCard
import com.arbuzerxxl.vibeshot.ui.R

@OptIn(KoinExperimentalAPI::class)
@Composable
fun InterestsRoute(
    modifier: Modifier = Modifier,
) {
    rememberKoinModules(unloadOnForgotten = true) { listOf(interestsModule) }

    val viewmodel: InterestsViewModel = koinViewModel()
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    InterestsScreen(
        modifier = modifier,
        uiState = uiState,
    )
}

@Composable
internal fun InterestsScreen(
    modifier: Modifier = Modifier,
    uiState: InterestsUiState,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            InterestsUiState.Loading -> LoadingIndicator()

            is InterestsUiState.Success -> {

//                PhotoCard(
//                    urlLowQuality = uiState.photos.first().lowQualityUrl,
//                    urlHighQuality = uiState.photos.first().highQualityUrl
//                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 360.dp, max = 5000.dp), // TODO max height will be fix
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(uiState.photos) { photo ->
                        PhotoCard(
                            urlLowQuality = photo.lowQualityUrl,
                            urlHighQuality = photo.highQualityUrl
                        )
                    }
                }
            }
        }
    }
}
