package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.storage.models.UserEntity
import com.arbuzerxxl.vibeshot.data.storage.models.UserType
import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.models.auth.User
import com.arbuzerxxl.vibeshot.domain.models.auth.tokens.AccessToken

class AuthDataMapper {

    fun mapToDomain(entity: UserEntity?): AuthState {
        return when {
            entity == null -> AuthState.Unauthenticated
            entity.type == UserType.GUEST
                -> AuthState.Guest(
                user = User(
                    id = entity.nsid, username = entity.userName,
                    fullname = entity.fullName,
                    token = null
                ))

            else
                -> AuthState.Authenticated(
                user = User(
                    id = entity.nsid, username = entity.userName,
                    fullname = entity.fullName,
                    token = AccessToken(
                        accessToken = entity.token!!,
                        accessTokenSecret = entity.tokenSecret!!
                    ) // TODO add keystore encoding
                )
            )
        }
    }

    fun mapToEntity(state: AuthState): UserEntity {
        return when (state) {
            is AuthState.Authenticated -> UserEntity(
                nsid = state.user.id,
                fullName = state.user.fullname,
                userName = state.user.username,
                token = state.user.token!!.accessToken,
                tokenSecret = state.user.token!!.accessTokenSecret,
                type = UserType.AUTHENTICATED
            )

            is AuthState.Guest -> UserEntity(
                nsid = state.user.id,
                fullName = state.user.fullname,
                userName = state.user.username,
                type = UserType.GUEST
            )

            AuthState.Unauthenticated -> throw IllegalStateException()
        }
    }
}