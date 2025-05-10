package com.arbuzerxxl.vibeshot.domain.models.photo

interface PhotoItem {
    val id: String
    val title: String
    val sizes: PhotoSizesResource
}