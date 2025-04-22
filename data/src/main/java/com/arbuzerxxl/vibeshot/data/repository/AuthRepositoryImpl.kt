package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.domain.repository.TokenRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val dispatcher: CoroutineDispatcher,
) : AuthRepository {

    override val authState: Flow<AuthState> = userRepository.observe().flowOn(dispatcher)

    override suspend fun getAuthUrl(): String = withContext(dispatcher) {

        tokenRepository.getAuthorizeUrlAndSaveRequestToken()
    }

    override suspend fun signIn(verifier: String) = withContext(dispatcher)  {
        val user = userRepository.getBy(verifier)
        userRepository.save(user)
    }

    override suspend fun signInAsGuest() = withContext(dispatcher)  {
        userRepository.saveAsGuest()
    }

    override suspend fun logOut() = withContext(dispatcher)  {
        userRepository.delete()
    }

}