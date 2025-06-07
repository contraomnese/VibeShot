package com.kiparo.pizzaapp.presentation.features.bottom_menu

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.core.navigation.TopDestinationsCollection
import com.arbuzerxxl.vibeshot.core.navigation.TopLevelDestination
import com.arbuzerxxl.vibeshot.core.ui.utils.ErrorMonitor
import com.arbuzerxxl.vibeshot.core.ui.utils.NetworkMonitor
import com.arbuzerxxl.vibeshot.core.ui.utils.NetworkStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val UNKNOWN_ERROR = "Unknown error"

@Immutable
internal data class BottomMenuUiState(
    val topLevelDestinations: ImmutableList<TopLevelDestination>,
    val networkStatus: NetworkStatus = NetworkStatus.Disconnected,
)

internal class BottomMenuViewModel(
    private val topLevelDestinations: TopDestinationsCollection,
    private val networkMonitor: NetworkMonitor,
    private val errorMonitor: ErrorMonitor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BottomMenuUiState(topLevelDestinations.items.toPersistentList()))
    val uiState = _uiState.asStateFlow()

    val errorEvents: Flow<String> = errorMonitor.errors

    init {
        updatingNetworkStatus()
    }

    private fun updatingNetworkStatus() {

        viewModelScope.launch {
            networkMonitor.networkStatus
                .catch { e ->
                    errorMonitor.tryEmit(e.message ?: UNKNOWN_ERROR)
                }
                .collect { status ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            networkStatus = status
                        )
                    }
                }
        }
    }
}