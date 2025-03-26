package com.arbuzerxxl.vibeshot.data.network.model

import com.google.gson.annotations.SerializedName

data class RequestToken(
    @SerializedName("oauth_callback_confirmed")
    val oauthCallbackConfirmed: Boolean,

    @SerializedName("oauth_token")
    val token: String,

    @SerializedName("oauth_token_secret")
    val tokenSecret: String,
)