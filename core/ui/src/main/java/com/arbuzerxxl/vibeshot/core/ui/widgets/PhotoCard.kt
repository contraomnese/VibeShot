package com.arbuzerxxl.vibeshot.core.ui.widgets

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PhotoCard(
    modifier: Modifier = Modifier,
    urlLowQuality: String,
    urlHighQuality: String,
) {

    Card(
        modifier = modifier
            .padding(10.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(),

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PhotoImage(
                urlLowQuality = urlLowQuality,
                urlHighQuality = urlHighQuality
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ItemCardPreview() {
//    PhotoCard(
//        urlLowQuality = "",
//        urlHighQuality = "",
//        placeholder = painterResource(R.drawable.compose_placeholder)
//    )
//}