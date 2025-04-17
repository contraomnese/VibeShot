package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight64
import com.arbuzerxxl.vibeshot.core.navigation.TopLevelDestination
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


@Composable
fun NavBar(
    currentDestination: String?,
    destinations: ImmutableList<TopLevelDestination>,
    onNavigateToTopLevel: (topRoute: Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = Modifier.height(itemHeight64),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        destinations.forEachIndexed { index, item ->
            NavigationBarItem(
                icon =
                    {
                        Box(modifier = Modifier.fillMaxHeight()) {
                            Icon(
                                modifier = Modifier.align(Alignment.Center),
                                imageVector = item.icon,
                                contentDescription = stringResource(item.titleId)
                            )
                        }
                    },
                selected = currentDestination == item.destinationRoute,
                onClick = { onNavigateToTopLevel(item.route) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledIconColor = Color.Transparent,
                    disabledTextColor = Color.Transparent,
                )
            )
        }
    }
}

@DevicePreviews
@Composable
fun NavBarPreview() {
    VibeShotTheme {
        NavBar(
            currentDestination = null,
            destinations = persistentListOf(),
            onNavigateToTopLevel = {})
    }
}