package com.arbuzerxxl.vibeshot.data.network.model.interestingness

import com.google.gson.annotations.SerializedName

data class InterestsPhotoNetwork(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    @SerializedName("ispublic") val isPublic: Int,
    @SerializedName("isfriend") val isFriend: Int,
    @SerializedName("isfamily") val isFamily: Int,
)