package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.storage.datastore.models.UserEntity
import com.arbuzerxxl.vibeshot.data.storage.datastore.models.UserType
import com.arbuzerxxl.vibeshot.domain.models.auth.User
import com.arbuzerxxl.vibeshot.domain.models.auth.tokens.AccessToken

fun UserEntity.toDomain(): User {
    return User(
        id = nsid, username = userName, fullname = fullName,
        token = if (token != null && tokenSecret != null) AccessToken(
            accessToken = token,
            accessTokenSecret = tokenSecret
        ) else null
    )
}

fun User.toStorage(): UserEntity {
    return UserEntity(
        nsid = id,
        fullName = fullname,
        userName = username,
        token = token?.accessToken ?: "",
        tokenSecret = token?.accessTokenSecret ?: "",
        type = UserType.AUTHENTICATED
    )
}