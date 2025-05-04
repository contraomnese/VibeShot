package com.arbuzerxxl.vibeshot.data.adapters


import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizes
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizesResponse
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class PhotoSizesResponseTypeAdapter : TypeAdapter<PhotoSizesResponse>() {

    override fun write(out: JsonWriter, value: PhotoSizesResponse) {
        throw UnsupportedOperationException("Serialization not supported")
    }

    override fun read(reader: JsonReader): PhotoSizesResponse {
        var stat: String? = null
        var code: Int? = null
        var message: String? = null
        var photoSizes: PhotoSizes? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "stat" -> stat = reader.nextString()
                "code" -> code = if (reader.peek() == JsonToken.NULL) null else reader.nextInt()
                "message" -> message = if (reader.peek() == JsonToken.NULL) null else reader.nextString()
                "sizes" -> {
                    if (reader.peek() != JsonToken.NULL) {
                        photoSizes = Gson().fromJson(reader, PhotoSizes::class.java)
                    } else {
                        reader.nextNull()
                    }
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return when (stat) {
            "ok" -> PhotoSizesResponse.Success(
                photoSizes ?: throw IllegalArgumentException("Sizes is missing in 'ok' response")
            )
            "fail" -> PhotoSizesResponse.Error(
                code ?: -1,
                message ?: "Unknown error"
            )
            else -> throw IllegalArgumentException("Unknown 'stat' value: $stat")
        }
    }
}