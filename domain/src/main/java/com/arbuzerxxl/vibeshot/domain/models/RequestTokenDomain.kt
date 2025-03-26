package com.arbuzerxxl.vibeshot.domain.models

data class RequestTokenDomain(
    val requestToken: String,
    val requestTokenSecret: String,
)