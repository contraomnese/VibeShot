package com.arbuzerxxl.vibeshot.domain.usecases.auth

import com.arbuzerxxl.vibeshot.domain.models.AccessTokenDomain
import com.arbuzerxxl.vibeshot.domain.models.RequestTokenDomain
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.domain.usecases.UseCaseWithoutParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetAccessTokenUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val authRepository: AuthRepository,
) {

    suspend fun execute(requestToken: String, verifier: String, secretToken: String): AccessTokenDomain =
        withContext(dispatcher) {
            authRepository.getAccessToken(
                requestToken = requestToken,
                verifier = verifier,
                secretToken = secretToken)
        }
}