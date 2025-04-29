package com.arbuzerxxl.vibeshot.domain.usecases

interface UseCaseWithParamsSuspend <R, P> {
    suspend fun execute(params: P) : R
}

interface UseCaseWithoutParamsSuspend <R> {
    suspend fun execute() : R
}

interface UseCaseWithParams <R, P> {
    fun execute(params: P) : R
}

interface UseCaseWithoutParams <R> {
    fun execute() : R
}

