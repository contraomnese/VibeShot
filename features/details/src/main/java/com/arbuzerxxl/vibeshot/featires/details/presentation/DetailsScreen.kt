package com.arbuzerxxl.vibeshot.featires.details.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.core.design.theme.bottomSheetHiddenOffset
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize2
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize28
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight4
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth40
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding24
import com.arbuzerxxl.vibeshot.core.design.theme.padding4
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun DetailsRoute(
    photo: DetailsPhoto,
    modifier: Modifier = Modifier,
    viewmodel: DetailsViewModel = koinViewModel(parameters = { parametersOf(photo) }),
) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    DetailsScreen(
        modifier = modifier,
        uiState = uiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun DetailsScreen(
    modifier: Modifier = Modifier,
    uiState: DetailsUiState,
) {
    val density = LocalDensity.current

    val decayAnimationSpec = exponentialDecay<Float>(
        frictionMultiplier = 3f
    )

    val state = remember {
        AnchoredDraggableState<SheetValue>(
            initialValue = SheetValue.Hidden,
            positionalThreshold = { with(density) { 56.dp.toPx() } },
            velocityThreshold = { with(density) { 125.dp.toPx() } },
            snapAnimationSpec = tween(
                durationMillis = TWEEN_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            ),
            decayAnimationSpec = decayAnimationSpec,
            confirmValueChange = { true }
        )
    }

    SubcomposeLayout { constraints ->

        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val sheetPlaceable = subcompose(slotId = LAYOUT_SHEET_ID) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = cornerSize28, topEnd = cornerSize28))
                    .background(MaterialTheme.colorScheme.surface)
                    .onSizeChanged {
                        val newAnchors = DraggableAnchors {
                            with(density) {
                                SheetValue.Hidden at (layoutHeight - bottomSheetHiddenOffset.toPx())
                                SheetValue.PartiallyExpanded at (layoutHeight * 0.4f)
                                SheetValue.Expanded at maxOf(layoutHeight * 0.2f, 0f)
                            }
                        }
                        state.updateAnchors(newAnchors, state.targetValue)
                    }
                    .animateContentSize(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding16)
                        .anchoredDraggable(state, orientation = Orientation.Vertical)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = padding4)
                            .clip(RoundedCornerShape(cornerSize2))
                            .width(itemWidth40)
                            .height(itemHeight4)

                            .align(Alignment.TopCenter)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    )
                    Text(
                        text = "Info",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = padding24)
                    )
                }
            }
        }.first().measure(constraints)


        val bodyPlaceable = subcompose(slotId = LAYOUT_BODY_ID) {
            Surface(modifier = modifier
                .fillMaxSize()
                .animateContentSize()) {
                when (uiState) {
                    DetailsUiState.Loading -> LoadingIndicator()
                    is DetailsUiState.Success -> PhotoImage(modifier = modifier, url = uiState.photo.url)
                }
            }
        }.first().measure(constraints.copy(
            maxHeight = with(density) { state.requireOffset().roundToInt() + cornerSize28.toPx().roundToInt()} ) )


        layout(layoutWidth, layoutHeight) {
            val sheetOffsetY = state.requireOffset().roundToInt()
            val sheetOffsetX = 0

            bodyPlaceable.place(x = 0, y = 0)
            sheetPlaceable.place(sheetOffsetX, sheetOffsetY)
        }
    }
}

private const val LAYOUT_SHEET_ID = "sheet"
private const val LAYOUT_BODY_ID = "body"
private const val TWEEN_ANIMATION_DURATION = 800

@DevicePreviews
@Composable
fun DetailsScreenPreview() {
    VibeShotTheme {
        DetailsScreen(
            uiState = DetailsUiState.Success(photo = DetailsPhoto(""))
        )
    }
}