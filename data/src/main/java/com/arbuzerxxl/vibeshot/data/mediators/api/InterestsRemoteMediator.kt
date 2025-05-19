package com.arbuzerxxl.vibeshot.data.mediators.api

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.arbuzerxxl.vibeshot.data.storage.db.photo.interests.dto.InterestsPhotoDto

@OptIn(ExperimentalPagingApi::class)
abstract class InterestsRemoteMediator: RemoteMediator<Int, InterestsPhotoDto>()