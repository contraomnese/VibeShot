package com.arbuzerxxl.vibeshot.data.adapters


import com.arbuzerxxl.vibeshot.data.network.model.interests.InterestsPhotos
import com.arbuzerxxl.vibeshot.data.network.model.interests.InterestsResponse
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class InterestsResponseTypeAdapter : TypeAdapter<InterestsResponse>() {

    override fun write(out: JsonWriter, value: InterestsResponse) {
        throw UnsupportedOperationException("Serialization not supported")
    }

    override fun read(reader: JsonReader): InterestsResponse {
        var stat: String? = null
        var code: Int? = null
        var message: String? = null
        var photos: InterestsPhotos? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "stat" -> stat = reader.nextString()
                "code" -> code = if (reader.peek() == JsonToken.NULL) null else reader.nextInt()
                "message" -> message = if (reader.peek() == JsonToken.NULL) null else reader.nextString()
                "photos" -> {
                    if (reader.peek() != JsonToken.NULL) {
                        photos = Gson().fromJson(reader, InterestsPhotos::class.java)
                    } else {
                        reader.nextNull()
                    }
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return when (stat) {
            "ok" -> InterestsResponse.Success(
                photos ?: throw IllegalArgumentException("Interests is missing in 'ok' response")
            )
            "fail" -> InterestsResponse.Error(
                code ?: -1,
                message ?: "Unknown error"
            )
            else -> throw IllegalArgumentException("Unknown 'stat' value: $stat")
        }
    }
}