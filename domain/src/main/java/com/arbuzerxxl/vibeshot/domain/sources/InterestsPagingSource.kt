package com.arbuzerxxl.vibeshot.domain.sources

import androidx.paging.PagingSource
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource

abstract class InterestsPagingSource: PagingSource<Int, InterestsPhotoResource>()