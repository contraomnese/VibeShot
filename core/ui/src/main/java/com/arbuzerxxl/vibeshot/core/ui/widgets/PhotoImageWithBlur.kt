package com.arbuzerxxl.vibeshot.core.ui.widgets


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest.Builder
import coil3.request.crossfade
import coil3.request.transformations
import com.commit451.coiltransformations.BlurTransformation


@Composable
fun PhotoImageWithBlur(
    modifier: Modifier = Modifier,
    urlLowQuality: String,
    urlHighQuality: String,
    height: Int,
    width: Int,
    isScrolling: Boolean,
) {

    val context = LocalContext.current

    val lowQualityPainter = rememberAsyncImagePainter(
        model = Builder(context)
            .data(urlLowQuality)
            .transformations(BlurTransformation(context = context, radius = 2f))
            .size(height = height, width = width)
            .build()
    )

    val state by lowQualityPainter.state.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {

        when (state) {
            AsyncImagePainter.State.Empty -> Unit
            is AsyncImagePainter.State.Error -> Unit
            is AsyncImagePainter.State.Loading -> Unit
            is AsyncImagePainter.State.Success,
                ->
                if (isScrolling)
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = lowQualityPainter,
                        contentDescription = "High quality image",
                        contentScale = ContentScale.Crop,
                    )
                else
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = Builder(context)
                            .data(urlHighQuality)
                            .placeholderMemoryCacheKey((state as AsyncImagePainter.State.Success).result.memoryCacheKey)
                            .size(height = height, width = width)
                            .crossfade(true)
                            .build(),
                        contentDescription = "High quality preview",
                        contentScale = ContentScale.Crop,
                    )
        }
    }
}

