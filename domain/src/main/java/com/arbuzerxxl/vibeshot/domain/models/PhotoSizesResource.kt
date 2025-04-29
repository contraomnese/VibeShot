package com.arbuzerxxl.vibeshot.domain.models

data class PhotoSizesResource(
    val width: Int,
    val height: Int,
    val highQualityUrl: String,
    val lowQualityUrl: String,
)
