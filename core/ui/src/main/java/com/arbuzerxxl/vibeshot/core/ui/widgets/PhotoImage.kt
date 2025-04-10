package com.arbuzerxxl.vibeshot.core.ui.widgets


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import com.arbuzerxxl.vibeshot.core.ui.effects.BlurTransformation
import com.arbuzerxxl.vibeshot.ui.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImagePainter.State.Empty.painter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@Composable
fun PhotoImage(
    modifier: Modifier = Modifier,
    urlLowQuality: String,
    urlHighQuality: String,
) {

//    val context = LocalContext.current
//    val painter = rememberAsyncImagePainter(
//        ImageRequest.Builder(context)
//            .data(urlHighQuality)
//            .listener(
//                onStart = { Log.d("Coil", "Start loading: $urlHighQuality") },
//                onSuccess = { _, _ -> Log.d("Coil", "Success: $urlHighQuality") },
//                onError = { _, throwable ->
//                    Log.e("Coil", "Error: ${throwable.throwable.message}")
//                }
//            )
//
//            .build()
//    )
//
//    val state by painter.state.collectAsState()
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
////                placeholder = painterResource(R.drawable.placeholder_bg)
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
            .crossfade(true)
            .build()
    )

    // Состояние загрузки

    val state by highQualityPainter.state.collectAsState()

    Box(modifier = modifier) {
        when {
            // Показываем размытое превью при загрузке
            state is AsyncImagePainter.State.Loading ||
                    state is AsyncImagePainter.State.Error -> {
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
            state is AsyncImagePainter.State.Success -> {
                Image(
                    painter = highQualityPainter,
                    contentDescription = "High quality image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Индикатор загрузки (опционально)
        if (state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

