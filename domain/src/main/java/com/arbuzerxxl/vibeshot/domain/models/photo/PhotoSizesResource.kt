package com.arbuzerxxl.vibeshot.domain.models.photo

data class PhotoSizesResource(
    val width: Int,
    val height: Int,
    val highQualityUrl: String,
    val lowQualityUrl: String,
)