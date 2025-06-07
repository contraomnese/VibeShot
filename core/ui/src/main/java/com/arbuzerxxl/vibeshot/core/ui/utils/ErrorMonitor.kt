package com.arbuzerxxl.vibeshot.core.ui.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ErrorMonitor {

    private val _errorChannel = Channel<String>(Channel.BUFFERED)
    val errors = _errorChannel.receiveAsFlow()

    suspend fun emit(message: String) {
        _errorChannel.send(message)
    }

    fun tryEmit(message: String) {
        _errorChannel.trySend(message)
    }
}