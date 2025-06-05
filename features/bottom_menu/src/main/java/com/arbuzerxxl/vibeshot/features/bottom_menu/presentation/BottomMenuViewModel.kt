package com.kiparo.pizzaapp.presentation.features.bottom_menu

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.core.navigation.TopDestinationsCollection
import com.arbuzerxxl.vibeshot.core.navigation.TopLevelDestination
import com.arbuzerxxl.vibeshot.core.ui.utils.NetworkMonitor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@Immutable
internal data class BottomMenuUiState(
    val topLevelDestinations: ImmutableList<TopLevelDestination>,
    val isNetworkConnected: Boolean = false,
)

internal class BottomMenuViewModel(
    private val topLevelDestinations: TopDestinationsCollection,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BottomMenuUiState(topLevelDestinations.items.toPersistentList()))
    val uiState = _uiState.asStateFlow()

    init {
        updatingNetworkStatus()
    }

    private fun updatingNetworkStatus() {

        val isConnected = networkMonitor.isConnected
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                true
            )

        viewModelScope.launch {
            isConnected.collect { value ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isNetworkConnected = value
                    )
                }
            }
        }
    }

}