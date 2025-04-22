package com.arbuzerxxl.vibeshot.data.network.model.photos

import com.google.gson.annotations.SerializedName

data class PhotoSizesNetwork(
    @SerializedName("size")
    val sizes: List<PhotoSizeNetwork>)
