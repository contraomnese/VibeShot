package com.arbuzerxxl.vibeshot.core.ui.widgets


import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.arbuzerxxl.vibeshot.core.ui.effects.BlurTransformation
import com.arbuzerxxl.vibeshot.ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

@Composable
fun PhotoImage(
    modifier: Modifier = Modifier,
    urlLowQuality: String,
    urlHighQuality: String,
) {

//    val context = LocalContext.current
//    val painter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(context)
//            .data(urlHighQuality)
//            .listener(
//                onStart = { Log.d("Coil", "Start loading: $urlHighQuality") },
//                onSuccess = { _, _ -> Log.d("Coil", "Success: $urlHighQuality") },
//                onError = { _, throwable ->
//                    Log.e("Coil", "Error: ${throwable.throwable.message}")
//                }
//            )
//            .build()
//    )
//
//    var state = painter.state
//
//    when (state) {
//        is AsyncImagePainter.State.Empty,
//        is AsyncImagePainter.State.Loading,
//            -> {
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(urlLowQuality)
//                    .crossfade(true)
//                    .transformations(BlurTransformation())
//                    .build(),
//                contentDescription = "Low quality image",
//                placeholder = painterResource(R.drawable.placeholder_bg)
//            )
//        }
//
//        is AsyncImagePainter.State.Success -> {
//            Image(
//                painter = painter,
//                contentDescription = "High quality image",
//            )
//        }
//
//        is AsyncImagePainter.State.Error -> {
//            // Show some error UI.
//        }
//    }

    val context = LocalContext.current

    val highQualityPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(urlHighQuality)
            .listener(
                onStart = { Log.d("Coil", "Start loading: $urlHighQuality") },
                onSuccess = { _, _ -> Log.d("Coil", "Success: $urlHighQuality") },
                onError = { _, throwable ->
                    Log.e("Coil", "Error: ${throwable.throwable.message}")
                }
            )
            .crossfade(true)
            .build()
    )

    // Состояние загрузки

    val _st = MutableStateFlow(highQualityPainter.state)
    val _state = _st.asStateFlow()

    val photo by _state.collectAsState()

    Box(modifier = modifier) {
        when {
            // Показываем размытое превью при загрузке
            photo is AsyncImagePainter.State.Loading ||
                    photo is AsyncImagePainter.State.Error -> {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(urlLowQuality)
                        .crossfade(true)
                        .transformations(BlurTransformation())
                        .build(),
                    contentDescription = "Low quality preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

            }

            // Показываем основное изображение
            photo is AsyncImagePainter.State.Success -> {
                Image(
                    painter = highQualityPainter,
                    contentDescription = "High quality image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Индикатор загрузки (опционально)
        if (photo is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

