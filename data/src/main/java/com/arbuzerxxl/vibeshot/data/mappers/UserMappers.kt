package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.storage.models.UserEntity
import com.arbuzerxxl.vibeshot.data.storage.models.UserType
import com.arbuzerxxl.vibeshot.domain.models.auth.User
import com.arbuzerxxl.vibeshot.domain.models.auth.tokens.AccessToken

fun UserEntity.toDomain(): User {
    return User(
        id = nsid, username = userName, fullname = fullName, token = AccessToken(
            accessToken = token,
            accessTokenSecret = tokenSecret
        )
    )
}

fun User.toStorage(): UserEntity {
    return UserEntity(
        nsid = id,
        fullName = fullname,
        userName = username,
        token = token.accessToken,
        tokenSecret = token.accessTokenSecret,
        type = UserType.AUTHENTICATED
    )
}