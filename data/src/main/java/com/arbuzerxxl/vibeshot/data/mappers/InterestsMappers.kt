package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestsPhotoNetwork
import com.arbuzerxxl.vibeshot.domain.models.InterestsResourceItem

fun InterestsPhotoNetwork.toDomain(): InterestsResourceItem {
    println("Title: ${this.title} M: ${this.urlM} L: ${this.urlL}")
    return InterestsResourceItem(
        title = title, url_m = urlM!!, url_l = urlL ?: urlM
    )
}

