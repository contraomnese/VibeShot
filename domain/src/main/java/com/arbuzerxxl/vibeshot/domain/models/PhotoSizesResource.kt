package com.arbuzerxxl.vibeshot.domain.models

data class PhotoSizesResource(
    val originalWidth: Int,
    val originalHeight: Int,
    val originalUrl: String,
    val highQualityWidth: Int,
    val highQualityHeight: Int,
    val highQualityUrl: String,
    val lowQualityUrl: String,
)
