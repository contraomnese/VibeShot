package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.mappers.AuthDataMapper
import com.arbuzerxxl.vibeshot.data.storage.api.UserStorage
import com.arbuzerxxl.vibeshot.data.storage.models.UserEntity
import com.arbuzerxxl.vibeshot.data.storage.models.UserType
import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.repository.TokenRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class UserRepositoryImpl(
    private val userStorage: UserStorage,
    private val tokenRepository: TokenRepository,
    private val mapper: AuthDataMapper,
) : UserRepository {

    override fun observe(): Flow<AuthState> = userStorage.observe().map { mapper.mapToDomain(it) }

    override suspend fun saveUser(verifier: String) {

        val response = tokenRepository.getAccessToken(verifier)

        userStorage.save(
            response.let {
                UserEntity(
                    nsid = it.userId,
                    userName = it.username,
                    fullName = it.fullname,
                    token = it.oAuthToken,
                    tokenSecret = it.oAuthTokenSecret,
                    type = UserType.AUTHENTICATED
                )
            })
    }

    override suspend fun saveUserAsGuest() {

        userStorage.save(
                UserEntity(
                    nsid = UUID.randomUUID().toString(),
                    type = UserType.GUEST,
                    userName = "Guest",
                    fullName = "Guest"
                )
            )
    }

    override suspend fun delete() {
        TODO("Not yet implemented")
    }
}