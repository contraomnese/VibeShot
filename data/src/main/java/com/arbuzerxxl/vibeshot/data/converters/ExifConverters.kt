package com.arbuzerxxl.vibeshot.data.converters

import androidx.room.TypeConverter
import com.arbuzerxxl.vibeshot.data.network.model.photos.ExifData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExifConverters {
    @TypeConverter
    fun fromExif(tags: List<ExifData>?): String? {
        return Gson().toJson(tags)
    }

    @TypeConverter
    fun toExif(json: String?): List<ExifData>? {
        return Gson().fromJson(json, object : TypeToken<List<ExifData>>() {}.type)
    }
}