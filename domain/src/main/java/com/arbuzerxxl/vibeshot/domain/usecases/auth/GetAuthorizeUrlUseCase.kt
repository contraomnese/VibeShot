package com.arbuzerxxl.vibeshot.domain.usecases.auth

import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.domain.usecases.UseCaseWithoutParams

class GetAuthorizeUrlUseCase(private val authRepository: AuthRepository) {

    fun execute(requestToken: String): String = authRepository.getAuthorizeUrl(requestToken)

}