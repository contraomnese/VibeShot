package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestsPhotoNetwork
import com.arbuzerxxl.vibeshot.domain.models.InterestsResourceItem

fun InterestsPhotoNetwork.toDomain(): InterestsResourceItem {
    return InterestsResourceItem(
        title = title,
        lowQualityImageUrl = urlS!!,
        highQualityImageUrl = urlL ?: urlS,
        height = heightL ?: heightS!!,
        width = widthL ?: widthS!!
    )
}

