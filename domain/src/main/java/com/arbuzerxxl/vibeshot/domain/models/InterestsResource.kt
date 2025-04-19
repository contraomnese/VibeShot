package com.arbuzerxxl.vibeshot.domain.models

data class InterestsResource(
    val title: String,
    val lowQualityImageUrl: LowQualityInterestsPhotoUrl,
    val highQualityImageUrl: HighQualityInterestsPhotoUrl,
    val height: Int,
    val width: Int
)

@JvmInline
value class LowQualityInterestsPhotoUrl(val url: String)

@JvmInline
value class HighQualityInterestsPhotoUrl(val url: String)
