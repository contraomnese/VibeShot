package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestingnessPhotoNetwork
import com.arbuzerxxl.vibeshot.domain.models.InterestingnessResourceItem

fun InterestingnessPhotoNetwork.toDomain(): InterestingnessResourceItem =
    InterestingnessResourceItem(
        title = title, url = urlM!!
    )