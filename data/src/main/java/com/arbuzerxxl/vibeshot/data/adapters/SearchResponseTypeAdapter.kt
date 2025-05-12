package com.arbuzerxxl.vibeshot.data.adapters


import com.arbuzerxxl.vibeshot.data.network.model.search.SearchPhotos
import com.arbuzerxxl.vibeshot.data.network.model.search.SearchResponse
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class SearchResponseTypeAdapter : TypeAdapter<SearchResponse>() {

    override fun write(out: JsonWriter, value: SearchResponse) {
        throw UnsupportedOperationException("Serialization not supported")
    }

    override fun read(reader: JsonReader): SearchResponse {
        var stat: String? = null
        var code: Int? = null
        var message: String? = null
        var photos: SearchPhotos? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "stat" -> stat = reader.nextString()
                "code" -> code = if (reader.peek() == JsonToken.NULL) null else reader.nextInt()
                "message" -> message = if (reader.peek() == JsonToken.NULL) null else reader.nextString()
                "photos" -> {
                    if (reader.peek() != JsonToken.NULL) {
                        photos = Gson().fromJson(reader, SearchPhotos::class.java)
                    } else {
                        reader.nextNull()
                    }
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return when (stat) {
            "ok" -> SearchResponse.Success(
                photos ?: throw IllegalArgumentException("Search photos is missing in 'ok' response")
            )
            "fail" -> SearchResponse.Error(
                code ?: -1,
                message ?: "Unknown error"
            )
            else -> throw IllegalArgumentException("Unknown 'stat' value: $stat")
        }
    }
}