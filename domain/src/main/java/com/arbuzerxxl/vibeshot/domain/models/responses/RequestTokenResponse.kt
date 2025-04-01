package com.arbuzerxxl.vibeshot.domain.models.responses

data class RequestTokenResponse(
    val oAuthToken: String,
    val oAuthTokenSecret: String,
)
