package com.arbuzerxxl.vibeshot.data.converters

import androidx.room.TypeConverter
import com.arbuzerxxl.vibeshot.data.network.model.photos.Tag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TagsConverters {
    @TypeConverter
    fun fromTags(tags: List<Tag>): String {
        return Gson().toJson(tags)
    }

    @TypeConverter
    fun toTags(json: String): List<Tag> {
        return Gson().fromJson(json, object : TypeToken<List<Tag>>() {}.type)
    }
}