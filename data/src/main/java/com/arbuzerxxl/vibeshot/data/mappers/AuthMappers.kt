package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.RequestToken
import com.arbuzerxxl.vibeshot.domain.models.RequestTokenDomain

fun RequestToken.toDomain(): RequestTokenDomain =
    RequestTokenDomain(
        requestToken = token,
        requestTokenSecret = tokenSecret
    )