package com.arbuzerxxl.vibeshot.data.storage.models

enum class UserType {
    GUEST,
    AUTHENTICATED
}

data class UserEntity(
    val nsid: String,
    val fullName: String = "",
    val userName: String = "",
    val token: String = "",
    val tokenSecret: String = "",
    val type: UserType
)
