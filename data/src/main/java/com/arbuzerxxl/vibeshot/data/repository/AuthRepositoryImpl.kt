package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.domain.repository.TokenRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val dispatcher: CoroutineDispatcher,
) : AuthRepository {

    override val authState: Flow<AuthState> = userRepository.observe()

    override suspend fun getAuthUrl(): String =
        tokenRepository.getAuthorizeUrl()


    override suspend fun signIn(verifier: String) {
        userRepository.saveUser(verifier)
    }

    override suspend fun signInAsGuest() {
        userRepository.saveUserAsGuest()
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

}