package com.arbuzerxxl.vibeshot.data.network.model.interestingness

import com.google.gson.annotations.SerializedName

data class PhotoDescriptionNetwork(
    @SerializedName("_content") val content: String
)