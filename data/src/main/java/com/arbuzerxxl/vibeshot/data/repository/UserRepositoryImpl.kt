package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.mappers.AuthDataMapper
import com.arbuzerxxl.vibeshot.data.mappers.toStorage
import com.arbuzerxxl.vibeshot.data.storage.api.UserStorage
import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.models.auth.User
import com.arbuzerxxl.vibeshot.domain.repository.TokenRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userStorage: UserStorage,
    private val tokenRepository: TokenRepository,
    private val mapper: AuthDataMapper,
) : UserRepository {

    override fun observe(): Flow<AuthState> = userStorage.user.map { mapper.mapToDomain(it) }

    override suspend fun getBy(verifier: String): User = tokenRepository.getUserBy(verifier)

    override suspend fun save(user: User) = userStorage.save(user.toStorage())

    override suspend fun saveAsGuest() = userStorage.saveAsGuest()

    override suspend fun delete() = userStorage.delete()
}