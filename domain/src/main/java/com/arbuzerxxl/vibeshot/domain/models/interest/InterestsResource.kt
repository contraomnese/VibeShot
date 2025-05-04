package com.arbuzerxxl.vibeshot.domain.models.interest

import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource

data class InterestsResource(
    val id: String,
    val title: String,
    val sizes: PhotoSizesResource,
)