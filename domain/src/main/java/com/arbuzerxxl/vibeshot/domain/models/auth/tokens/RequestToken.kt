package com.arbuzerxxl.vibeshot.domain.models.auth.tokens

data class RequestToken(
    val token: String,
    val secret: String,
)