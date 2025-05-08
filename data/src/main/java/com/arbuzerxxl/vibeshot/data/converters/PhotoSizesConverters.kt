package com.arbuzerxxl.vibeshot.data.converters

import androidx.room.TypeConverter
import com.arbuzerxxl.vibeshot.data.network.model.photos.Size
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PhotoSizesConverters {
    @TypeConverter
    fun fromSizes(tags: List<Size>): String {
        return Gson().toJson(tags)
    }

    @TypeConverter
    fun toSizes(json: String): List<Size> {
        return Gson().fromJson(json, object : TypeToken<List<Size>>() {}.type)
    }
}