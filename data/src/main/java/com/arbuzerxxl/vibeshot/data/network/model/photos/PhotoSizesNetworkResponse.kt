package com.arbuzerxxl.vibeshot.data.network.model.photos

import com.google.gson.annotations.SerializedName

data class PhotoSizesNetworkResponse(
    @SerializedName("sizes")
    val response: PhotoSizesNetwork,
)
