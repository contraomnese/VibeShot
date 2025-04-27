package com.arbuzerxxl.vibeshot.domain.models

data class PhotoSizesResource(
    val originalUrl: String,
    val width: Int,
    val height: Int,
    val highQualityUrl: String,
    val lowQualityUrl: String,
)
