package com.arbuzerxxl.vibeshot.featires.details.presentation

import android.util.Log
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
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.arbuzerxxl.vibeshot.core.design.theme.bottomSheetHiddenOffset
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize2
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize28
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight4
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth40
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding24
import com.arbuzerxxl.vibeshot.core.design.theme.padding4
import com.arbuzerxxl.vibeshot.core.design.theme.zero
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.math.roundToInt

private const val LAYOUT_SHEET_ID = "sheet"
private const val LAYOUT_BODY_ID = "body"
private const val TWEEN_ANIMATION_DURATION = 800

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun DetailsRoute(
    initialPhotoIndex: DetailsPhotoIndex,
    modifier: Modifier = Modifier,
    viewmodel: DetailsViewModel = koinViewModel(),
) {

    viewmodel.initialize(initialPhotoIndex.index)

    val uiState: DetailsUiState by viewmodel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        DetailsUiState.Loading -> LoadingIndicator()
        is DetailsUiState.Success -> {
            val items = (uiState as DetailsUiState.Success).photos.collectAsLazyPagingItems()
            DetailsScreen(
                modifier = modifier,
                items = items,
                initialPhotoIndex = initialPhotoIndex.index
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun DetailsScreen(
    initialPhotoIndex: Int,
    modifier: Modifier = Modifier,
    items: LazyPagingItems<DetailsPhoto>,
) {

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialPhotoIndex)

    LaunchedEffect(Unit) {
        listState.scrollToItem(initialPhotoIndex)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (items.itemSnapshotList.isNotEmpty()) {

            if (items.loadState.refresh != LoadState.Loading && items.loadState.append != LoadState.Loading) {

                val currentPhoto by remember(listState) {
                    derivedStateOf {
                        items[listState.firstVisibleItemIndex]
                    }
                }

                ScreenContent(
                    uiState = items,
                    currentPhoto = currentPhoto,
                    initialIndex = initialPhotoIndex,
                    listState = listState
                )

                Log.d(
                    "Status",
                    "Photos count: ${items.itemCount} Initial index: $initialPhotoIndex"
                )
            }
            else {
                LoadingIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    uiState: LazyPagingItems<DetailsPhoto>,
    currentPhoto: DetailsPhoto?,
    initialIndex: Int,
    listState: LazyListState
) {

    val density = LocalDensity.current

    val anchoredDraggableState = remember {
        AnchoredDraggableState<SheetValue>(
            initialValue = SheetValue.Hidden
        )
    }

    SubcomposeLayout { constraints ->

        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val sheetPlaceable = subcompose(slotId = LAYOUT_SHEET_ID) {
            SheetContent(
                layoutHeight = layoutHeight,
                state = anchoredDraggableState,
                currentPhoto = currentPhoto
            )
        }.first().measure(constraints)


        val bodyPlaceable = subcompose(slotId = LAYOUT_BODY_ID) {
            BodyContent(
                items = uiState,
                initialIndex = initialIndex,
                itemWidth = layoutWidth.dp,
                listState = listState
            )
        }.first().measure(
            constraints.copy(
                maxHeight = with(density) {
                    anchoredDraggableState.requireOffset().roundToInt() + cornerSize28.toPx().roundToInt()
                })
        )


        layout(layoutWidth, layoutHeight) {
            val sheetOffsetY = anchoredDraggableState.requireOffset().roundToInt()
            val sheetOffsetX = 0

            bodyPlaceable.place(x = 0, y = 0)
            sheetPlaceable.place(sheetOffsetX, sheetOffsetY)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetContent(
    modifier: Modifier = Modifier,
    state: AnchoredDraggableState<SheetValue>,
    layoutHeight: Int,
    currentPhoto: DetailsPhoto?,
) {

    val density = LocalDensity.current

    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = state,
        positionalThreshold = { with(density) { 56.dp.toPx() } },
        animationSpec = tween(
            durationMillis = TWEEN_ANIMATION_DURATION,
            easing = LinearOutSlowInEasing
        )
    )

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
                modifier = Modifier
                    .padding(top = padding24)
                    .align(Alignment.TopCenter)
            )
        }
    }
}


@Composable
fun BodyContent(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<DetailsPhoto>,
    itemWidth: Dp,
    initialIndex: Int,
    listState: LazyListState
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            state = listState,
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(zero),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
        ) {

            if (items.loadState.refresh == LoadState.Loading) {
                item { LoadingIndicator() }
            }
            if (items.loadState.append == LoadState.Loading) {
                item {
                    LoadingIndicator(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp)
                            .size(20.dp)
                    )
                }
            }
            items(
                count = items.itemCount,
                key = items.itemKey { it.id }
            ) { item ->
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .width(itemWidth)
                ) {
                    val photo = items[item]
                    photo?.let {
                        Surface(
                            modifier = modifier
                                .fillMaxSize()
                                .animateContentSize()
                        ) {
                            PhotoImage(modifier = modifier, url = photo.url)
                        }
                    }
                }
            }
        }
    }
}