package com.arbuzerxxl.vibeshot.domain.models

data class PhotoSizesResource(
    val originalWidth: Int,
    val originalHeight: Int,
    val originalUrl: String,
    val width: Int,
    val height: Int,
    val url: String,
    val lowQualityUrl: String,
)
