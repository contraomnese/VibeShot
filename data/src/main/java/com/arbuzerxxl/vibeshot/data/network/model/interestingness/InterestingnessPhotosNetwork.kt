package com.arbuzerxxl.vibeshot.data.network.model.interestingness

data class InterestingnessPhotosNetwork(
    val photos: List<InterestingnessPhotoNetwork>,
    val page: String,
    val pages: String,
    val perpage: String,
    val total: String
)
