package com.arbuzerxxl.vibeshot.featires.details.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arbuzerxxl.vibeshot.core.design.theme.bottomSheetHiddenOffset
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize2
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize28
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight4
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth40
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding24
import com.arbuzerxxl.vibeshot.core.design.theme.padding4
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun DetailsRoute(
    initialPhotoId: DetailsPhotoId,
    modifier: Modifier = Modifier,
    viewmodel: DetailsViewModel = koinViewModel(parameters = { parametersOf(initialPhotoId) }),
) {

    val uiState = viewmodel.uiState.collectAsLazyPagingItems()

    DetailsScreen(
        modifier = modifier,
        uiState = uiState,
        initialPhotoId = initialPhotoId.id
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun DetailsScreen(
    initialPhotoId: String,
    modifier: Modifier = Modifier,
    uiState: LazyPagingItems<DetailsPhoto>,
) {

    var isInitialized by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(
        pageCount = { uiState.itemCount }
    )

    LaunchedEffect(uiState.itemSnapshotList) {

        if (!isInitialized) {
            val initialIndex = uiState.itemSnapshotList.items.indexOfFirst { it.id == initialPhotoId }

            if (initialIndex != -1 && pagerState.currentPage != initialIndex) {
                pagerState.scrollToPage(initialIndex)
                isInitialized = true
            }
        }
    }

    val currentPhoto by remember(uiState.itemSnapshotList, pagerState.currentPage) {
        derivedStateOf {
            uiState.itemSnapshotList.getOrNull(pagerState.currentPage)
        }
    }

    ScreenContent(photos = uiState, pagerState = pagerState, currentPhoto = currentPhoto)

}

private const val LAYOUT_SHEET_ID = "sheet"
private const val LAYOUT_BODY_ID = "body"
private const val TWEEN_ANIMATION_DURATION = 800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<DetailsPhoto>,
    pagerState: PagerState,
    currentPhoto: DetailsPhoto?
) {

    val density = LocalDensity.current

    val state = remember {
        AnchoredDraggableState<SheetValue>(
            initialValue = SheetValue.Hidden
        )
    }

    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = state,
        positionalThreshold = { with(density) { 56.dp.toPx() } },
        animationSpec = tween(
            durationMillis = TWEEN_ANIMATION_DURATION,
            easing = LinearOutSlowInEasing
        )
    )

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
                        .anchoredDraggable(state, orientation = Orientation.Vertical, flingBehavior = flingBehavior)
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
                        text = currentPhoto?.title ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = padding24).align(Alignment.TopCenter)
                    )
                }
            }
        }.first().measure(constraints)


        val bodyPlaceable = subcompose(slotId = LAYOUT_BODY_ID) {
            HorizontalPager(
                state = pagerState,
            ) { index ->
                val photo = photos[index]
                photo?.let {
                    Surface(modifier = modifier
                        .fillMaxSize()
                        .animateContentSize()) {
                        PhotoImage(modifier = modifier, url = photo.url)
                    }
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

//@DevicePreviews
//@Composable
//fun DetailsScreenPreview() {
//    VibeShotTheme {
//        DetailsScreen(
//            uiState = DetailsUiState.Success(photo = DetailsPhoto(""))
//        )
//    }
//}