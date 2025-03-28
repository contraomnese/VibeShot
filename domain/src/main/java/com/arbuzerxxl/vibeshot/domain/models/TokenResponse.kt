package com.arbuzerxxl.vibeshot.domain.models

data class TokenResponse(
    val oAuthToken: String? = null,
    val oAuthTokenSecret: String? = null,
    val fullName: String? = null,
    val userId: String? = null,
    val userName: String? = null
)
