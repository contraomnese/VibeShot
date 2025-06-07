package com.arbuzerxxl.vibeshot.features.details.presentation


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.bottomSheetHiddenOffset
import com.arbuzerxxl.vibeshot.core.design.theme.cornerRadius8
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize2
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize28
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight4
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight40
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth40
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding160
import com.arbuzerxxl.vibeshot.core.design.theme.padding24
import com.arbuzerxxl.vibeshot.core.design.theme.padding4
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.design.theme.padding80
import com.arbuzerxxl.vibeshot.core.design.theme.zero
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.core.ui.utils.NetworkStatus
import com.arbuzerxxl.vibeshot.core.ui.widgets.CameraCard
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.NetworkDisconnectionBanner
import com.arbuzerxxl.vibeshot.core.ui.widgets.OwnerItem
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoDetailsItem
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoImage
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoImagePlaceholder
import com.arbuzerxxl.vibeshot.core.ui.widgets.TagItem
import com.arbuzerxxl.vibeshot.domain.models.photo.CameraResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.utils.formatDateTimeWithLocale
import com.arbuzerxxl.vibeshot.domain.utils.formatUnixTimeWithSystemLocale
import com.arbuzerxxl.vibeshot.features.details.navigation.ParentDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

private const val LAYOUT_SHEET_ID = "sheet"
private const val LAYOUT_BODY_ID = "body"
private const val TWEEN_ANIMATION_DURATION = 800


@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun DetailsRoute(
    onNavigateUp: () -> Unit,
    photoPosition: Int = 0,
    parentDestination: ParentDestination,
    modifier: Modifier = Modifier,
    viewmodel: DetailsViewModel = koinViewModel(parameters = { parametersOf(parentDestination) }),
) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val items = uiState.photos.collectAsLazyPagingItems()

    DetailsScreen(
        modifier = modifier,
        items = items,
        uiState = uiState,
        currentPhotoPosition = photoPosition,
        onUpdateCurrentPhoto = viewmodel::onUpdateCurrentPhoto
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun DetailsScreen(
    uiState: DetailsUiState,
    currentPhotoPosition: Int,
    modifier: Modifier = Modifier,
    items: LazyPagingItems<DetailsPhoto>,
    onUpdateCurrentPhoto: (DetailsPhoto) -> Unit,
) {

    val photosLazyListState = rememberLazyListState(initialFirstVisibleItemIndex = currentPhotoPosition)

    val currentPhotoInfo by remember(items) {
        derivedStateOf {
            val currentPhotoIndex = photosLazyListState.firstVisibleItemIndex
            val isLoaded = items.loadState.refresh is LoadState.NotLoading
            if (isLoaded && items.itemSnapshotList.isNotEmpty()) items[currentPhotoIndex] else null
        }
    }

    LaunchedEffect(currentPhotoInfo) {
        currentPhotoInfo?.let {
            when (uiState.networkStatus) {
                is NetworkStatus.Connected -> onUpdateCurrentPhoto(it)
                is NetworkStatus.Disconnected -> Unit
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        ScreenContent(
            items = items,
            currentPhoto = uiState.currentPhoto,
            isLoading = uiState.isLoading,
            photosLazyListState = photosLazyListState
        )
        if (uiState.networkStatus == NetworkStatus.Disconnected)
            NetworkDisconnectionBanner(
                modifier = Modifier.padding(top = padding16)
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    items: LazyPagingItems<DetailsPhoto>,
    currentPhoto: PhotoResource?,
    isLoading: Boolean,
    photosLazyListState: LazyListState,
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

        val photoInfoLayout = subcompose(slotId = LAYOUT_SHEET_ID) {
            PhotoInfoLayout(
                layoutHeight = layoutHeight,
                draggableState = anchoredDraggableState,
                currentPhoto = currentPhoto,
                isLoading = isLoading,
            )
        }.first().measure(constraints)


        val photoLayout = subcompose(slotId = LAYOUT_BODY_ID) {
            PhotoLayout(
                items = items,
                itemWidth = layoutWidth.dp,
                listState = photosLazyListState,
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

            photoLayout.place(x = 0, y = 0)
            photoInfoLayout.place(sheetOffsetX, sheetOffsetY)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoInfoLayout(
    draggableState: AnchoredDraggableState<SheetValue>,
    currentPhoto: PhotoResource?,
    isLoading: Boolean,
    layoutHeight: Int,
) {

    val density = LocalDensity.current

    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = draggableState,
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
                        SheetValue.PartiallyExpanded at (layoutHeight * 0.6f)
                        SheetValue.Expanded at maxOf(layoutHeight * 0.15f, 0f)
                    }
                }
                draggableState.updateAnchors(newAnchors, draggableState.targetValue)
            },
    ) {
        PhotoInfoContent(
            state = draggableState,
            photo = currentPhoto,
            isLoading = isLoading,
            flingBehavior = flingBehavior,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoInfoContent(
    state: AnchoredDraggableState<SheetValue>,
    flingBehavior: TargetedFlingBehavior,
    photo: PhotoResource?,
    isLoading: Boolean,
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

        when {
            isLoading ->
                LoadingIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = padding80)
                        .height(
                            itemHeight40
                        )
                )

            else ->
                photo?.let {
                    Column(
                        modifier = Modifier.padding(top = padding16),
                        verticalArrangement = Arrangement.spacedBy(padding24)
                    ) {
                        OwnerBlock(
                            owner = it.owner,
                            iconUrl = it.ownerIconUrl
                        )
                        TitleBlock(
                            title = it.title,
                            description = it.description
                        )
                        MetaBlock(
                            dateUpload = it.dateUpload,
                            dateTaken = it.dateTaken,
                            views = it.views,
                            comments = it.comments
                        )
                        CameraBlock(camera = it.cameraResource)
                        TagsBlock(tags = it.tags)
                        MoreBlock(license = it.license)
                    }
                }
        }
    }
}

@Composable
private fun OwnerBlock(
    modifier: Modifier = Modifier,
    owner: String,
    iconUrl: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OwnerItem(
            owner = owner, url = iconUrl
        )
    }
}

@Composable
private fun TitleBlock(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (description.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = padding8),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Justify,
                minLines = 1,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MetaBlock(
    modifier: Modifier = Modifier,
    dateUpload: String,
    dateTaken: String,
    views: String,
    comments: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius8))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(padding8),
            verticalArrangement = Arrangement.spacedBy(padding16)
        ) {
            PhotoDetailsItem(
                icon = VibeShotIcons.Upload,
                text = dateUpload
            )
            PhotoDetailsItem(
                icon = VibeShotIcons.Calendar,
                text = dateTaken
            )
        }
        Column(
            modifier = Modifier.padding(start = padding160, top = padding8, end = padding8, bottom = padding8),
            verticalArrangement = Arrangement.spacedBy(padding16)
        ) {
            PhotoDetailsItem(
                icon = VibeShotIcons.Views,
                text = "$views Views"
            )
            PhotoDetailsItem(
                icon = VibeShotIcons.Comments,
                text = "$comments Comments"
            )
        }
    }
}

@Composable
private fun CameraBlock(
    modifier: Modifier = Modifier,
    camera: CameraResource?,
) {
    camera?.let {
        CameraCard(
            modifier = modifier,
            cameraModel = it.model,
            lensModel = it.lens,
            aperture = it.aperture,
            focalLength = it.focalLength,
            iso = it.iso,
            flash = it.flash,
            exposureTime = it.exposureTime,
            whiteBalance = it.whiteBalance
        )
    }
}

@Composable
private fun TagsBlock(
    modifier: Modifier = Modifier,
    tags: List<String>,
) {
    if (tags.isNotEmpty()) {
        val scrollState = rememberScrollState()

        Row(
            modifier = modifier
                .horizontalScroll(scrollState)
                .padding(vertical = padding8),
            horizontalArrangement = Arrangement.spacedBy(padding8)
        ) {
            tags.forEach { tag ->
                TagItem(tag = tag)
            }
        }
    }
}

@Composable
private fun MoreBlock(
    modifier: Modifier = Modifier,
    license: String,
) {
    Box(
        modifier = modifier
            .wrapContentWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(padding16)) {
            PhotoDetailsItem(
                icon = VibeShotIcons.Copyright,
                text = license
            )
        }
    }
}


@Composable
private fun PhotoLayout(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<DetailsPhoto>,
    itemWidth: Dp,
    listState: LazyListState,
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
                        PhotoImage(modifier = modifier, url = photo.url)
                    } ?: PhotoImagePlaceholder()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
private fun SheetContentPreview() {
    VibeShotThemePreview {

        val uiState = DetailsUiState(
            currentPhoto = PhotoResource(
                id = "1234",
                url = "www.ww.com",
                owner = "Gerd Michael Kozik",
                title = "L.A.K.E.",
                ownerIconUrl = "",
                description = " as websites, print, commercial or private use. Do not use my photos without my permission !\nThank you all for your ryyrtyrtyrtyrtyvisits, faves and comments!\n".trim(),
                dateUpload = "1746355286".formatUnixTimeWithSystemLocale(),
                dateTaken = "2025-04-28 22:01:58".formatDateTimeWithLocale(),
                views = "1345",
                comments = "64",
                cameraResource = CameraResource(
                    model = "Sony ILCE-7RM5",
                    lens = "50mm f/1.2",
                    aperture = "f/16.0",
                    focalLength = "50.0 mm",
                    iso = "",
                    flash = "Off, Did not fire",
                    exposureTime = "1/320",
                    whiteBalance = "Auto",
                ),
                tags = listOf<String>(
                    "lion1",
                    "twins1",
                    "Sister1",
                    "lion2",
                    "twins2",
                    "Sister2",
                    "lion3",
                    "twins3",
                    "Sister3",
                ),
                license = "All Rights Reserved"
            ),
            isLoading = false,
            networkStatus = NetworkStatus.Connected
        )

        PhotoInfoLayout(
            draggableState = AnchoredDraggableState<SheetValue>(initialValue = SheetValue.Hidden),
            layoutHeight = 2400,
            currentPhoto = uiState.currentPhoto,
            isLoading = uiState.isLoading
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
private fun PhotoInfoLayoutEmptyPreview() {
    VibeShotThemePreview {

        val uiState = DetailsUiState(
            isLoading = true,
            networkStatus = NetworkStatus.Connected
        )

        PhotoInfoLayout(
            draggableState = AnchoredDraggableState<SheetValue>(initialValue = SheetValue.Hidden),
            layoutHeight = 2400,
            currentPhoto = uiState.currentPhoto,
            isLoading = uiState.isLoading
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
private fun DetailsScreenNetworkIsDisabledPreview() {
    VibeShotThemePreview {

        val uiState = DetailsUiState()
        val items = uiState.photos.collectAsLazyPagingItems()

        DetailsScreen(
            uiState = uiState,
            currentPhotoPosition = 2,
            items = items,
            onUpdateCurrentPhoto = {}
        )
    }
}