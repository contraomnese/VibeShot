package com.arbuzerxxl.vibeshot.data.sources.model

import com.google.gson.annotations.SerializedName

data class SearchNetworkResponse(
    @SerializedName("photos")
    val response: SearchPhotosNetwork,
)
