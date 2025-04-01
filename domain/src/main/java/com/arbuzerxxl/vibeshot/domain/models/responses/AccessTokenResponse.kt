package com.arbuzerxxl.vibeshot.domain.models.responses

data class AccessTokenResponse(
    val oAuthToken: String,
    val oAuthTokenSecret: String,
    val fullname: String,
    val userId: String,
    val username: String
)
