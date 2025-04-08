package com.arbuzerxxl.vibeshot.domain.models.auth

import com.arbuzerxxl.vibeshot.domain.models.auth.tokens.AccessToken

data class User(
    val id: String,
    val username: String,
    val fullname: String,
    val token: AccessToken?
)