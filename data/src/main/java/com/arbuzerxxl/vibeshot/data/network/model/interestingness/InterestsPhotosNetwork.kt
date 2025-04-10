package com.arbuzerxxl.vibeshot.data.network.model.interestingness

data class InterestsPhotosNetwork(
    val photo: List<InterestsPhotoNetwork>,
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int
)
