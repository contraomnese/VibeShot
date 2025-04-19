package com.arbuzerxxl.vibeshot.data.network.model.interestingness

import com.google.gson.annotations.SerializedName

data class InterestsNetworkResponse(
    @SerializedName("photos")
    val response: InterestsPhotosNetwork,
)
