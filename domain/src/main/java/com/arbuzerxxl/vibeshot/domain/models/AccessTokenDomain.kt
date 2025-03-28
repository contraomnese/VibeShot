package com.arbuzerxxl.vibeshot.domain.models

data class AccessTokenDomain(
    val accessToken: String,
    val accessTokenSecret: String,
    val user: User
)