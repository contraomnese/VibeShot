package com.kiparo.pizzaapp.presentation.features.bottom_menu

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.arbuzerxxl.vibeshot.core.navigation.TopDestinationsCollection
import com.arbuzerxxl.vibeshot.core.navigation.TopLevelDestination


@Immutable
internal data class BottomMenuUiState(val topLevelDestinations: ImmutableList<TopLevelDestination>)

internal class BottomMenuViewModel(
    private val topLevelDestinations: TopDestinationsCollection
) : ViewModel() {

    private val _uiState = MutableStateFlow(BottomMenuUiState(topLevelDestinations.items.toPersistentList()))
    val uiState = _uiState.asStateFlow()

}