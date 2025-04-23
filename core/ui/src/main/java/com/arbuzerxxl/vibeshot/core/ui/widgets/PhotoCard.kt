package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arbuzerxxl.vibeshot.core.ui.utils.pxToDp


@Composable
fun PhotoCard(
    modifier: Modifier = Modifier,
    urlLowQuality: String,
    urlHighQuality: String,
    height: Int,
    width: Int,
    isScrolling: Boolean
) {

    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = pxToDp(context, height).dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(),

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PhotoImageWithBlur(
                urlLowQuality = urlLowQuality,
                urlHighQuality = urlHighQuality,
                height = height,
                width = width,
                isScrolling = isScrolling
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemCardPreview() {
    PhotoCard(
        urlLowQuality = "",
        urlHighQuality = "",
        height = 200,
        width = 400,
        isScrolling = false
    )
}