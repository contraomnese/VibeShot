package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestsPhotoNetwork
import com.arbuzerxxl.vibeshot.domain.models.InterestsResourceItem

fun InterestsPhotoNetwork.toDomain(): InterestsResourceItem =
    InterestsResourceItem(
        title = title, url = urlM!!
    )