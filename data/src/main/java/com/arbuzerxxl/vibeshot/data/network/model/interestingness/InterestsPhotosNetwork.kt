package com.arbuzerxxl.vibeshot.data.network.model.interestingness

import com.google.gson.annotations.SerializedName

data class InterestsPhotosNetwork(
    @SerializedName("photo")
    val photos: List<InterestsPhotoNetwork>,
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int
)
