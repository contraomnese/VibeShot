package com.arbuzerxxl.vibeshot.data.adapters

import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoExif
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoExifResponse
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class PhotoExifResponseTypeAdapter : TypeAdapter<PhotoExifResponse>() {

    override fun write(out: JsonWriter, value: PhotoExifResponse) {
        throw UnsupportedOperationException("Serialization not supported")
    }

    override fun read(reader: JsonReader): PhotoExifResponse {
        var stat: String? = null
        var code: Int? = null
        var message: String? = null
        var photo: PhotoExif? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "stat" -> stat = reader.nextString()
                "code" -> code = if (reader.peek() == JsonToken.NULL) null else reader.nextInt()
                "message" -> message = if (reader.peek() == JsonToken.NULL) null else reader.nextString()
                "photo" -> {
                    if (reader.peek() != JsonToken.NULL) {
                        photo = Gson().fromJson(reader, PhotoExif::class.java)
                    } else {
                        reader.nextNull()
                    }
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return when (stat) {
            "ok" -> PhotoExifResponse.Success(
                photo ?: throw IllegalArgumentException("Exif is missing in 'ok' response")
            )
            "fail" -> PhotoExifResponse.Error(
                code ?: -1,
                message ?: "Unknown error"
            )
            else -> throw IllegalArgumentException("Unknown 'stat' value: $stat")
        }
    }
}