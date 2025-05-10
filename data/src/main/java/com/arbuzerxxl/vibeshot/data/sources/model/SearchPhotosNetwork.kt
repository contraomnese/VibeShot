package com.arbuzerxxl.vibeshot.data.sources.model

import com.google.gson.annotations.SerializedName

data class SearchPhotosNetwork(
    @SerializedName("photo")
    val photos: List<SearchSource>,
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int
)
