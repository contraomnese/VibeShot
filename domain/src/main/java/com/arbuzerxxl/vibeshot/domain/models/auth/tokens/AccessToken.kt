package com.arbuzerxxl.vibeshot.domain.models.auth.tokens

data class AccessToken(
    val accessToken: String,
    val accessTokenSecret: String,
)