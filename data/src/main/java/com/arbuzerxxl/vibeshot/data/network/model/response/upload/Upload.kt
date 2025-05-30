package com.arbuzerxxl.vibeshot.data.network.model.response.upload

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rsp", strict = false)
data class FlickrUploadResponse(
    @field:Element(name = "photoid", required = false)
    var photoId: String? = null,

    @field:Attribute(name = "stat", required = false)
    var status: String? = null
)