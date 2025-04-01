package com.arbuzerxxl.vibeshot.domain.usecases.user

import com.arbuzerxxl.vibeshot.domain.models.auth.User
import com.arbuzerxxl.vibeshot.domain.repository.UserRepository
import com.arbuzerxxl.vibeshot.domain.usecases.UseCaseWithoutParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

//class GetUserUseCase(
//    private val dispatcher: CoroutineDispatcher,
//    private val userRepository: UserRepository,
//) : UseCaseWithoutParams<Flow<User?>> {
//
//    override suspend fun execute(): Flow<User?> = withContext(dispatcher) {
//        userRepository.observe()
//    }
//}

