package com.arbuzerxxl.vibeshot.data.adapters


import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoInfo
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoInfoResponse
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class PhotoInfoResponseTypeAdapter : TypeAdapter<PhotoInfoResponse>() {

    override fun write(out: JsonWriter, value: PhotoInfoResponse) {
        throw UnsupportedOperationException("Serialization not supported")
    }

    override fun read(reader: JsonReader): PhotoInfoResponse {
        var stat: String? = null
        var code: Int? = null
        var message: String? = null
        var info: PhotoInfo? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "stat" -> stat = reader.nextString()
                "code" -> code = if (reader.peek() == JsonToken.NULL) null else reader.nextInt()
                "message" -> message = if (reader.peek() == JsonToken.NULL) null else reader.nextString()
                "photo" -> {
                    if (reader.peek() != JsonToken.NULL) {
                        info = Gson().fromJson(reader, PhotoInfo::class.java)
                    } else {
                        reader.nextNull()
                    }
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return when (stat) {
            "ok" -> PhotoInfoResponse.Success(
                info ?: throw IllegalArgumentException("Info is missing in 'ok' response")
            )
            "fail" -> PhotoInfoResponse.Error(
                code ?: -1,
                message ?: "Unknown error"
            )
            else -> throw IllegalArgumentException("Unknown 'stat' value: $stat")
        }
    }
}