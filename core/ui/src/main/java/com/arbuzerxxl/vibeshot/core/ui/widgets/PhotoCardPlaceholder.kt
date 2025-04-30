package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight64


@Composable
fun PhotoCardPlaceHolder(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(itemHeight64),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(),
    ) {}
}

@Preview(showBackground = true)
@Composable
fun PhotoCardPlaceHolderPreview() {
    PhotoCardPlaceHolder()
}