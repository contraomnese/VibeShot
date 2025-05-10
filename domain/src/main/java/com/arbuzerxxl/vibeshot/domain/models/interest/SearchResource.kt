package com.arbuzerxxl.vibeshot.domain.models.interest

import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoItem
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource

data class SearchResource(
    override val id: String,
    override val title: String,
    override val sizes: PhotoSizesResource,
): PhotoItem

