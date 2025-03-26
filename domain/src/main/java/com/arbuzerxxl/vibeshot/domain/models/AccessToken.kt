package com.arbuzerxxl.vibeshot.domain.models

data class AccessToken(
    val accessToken: String,
    val accessTokenSecret: String,
    val user: User
)