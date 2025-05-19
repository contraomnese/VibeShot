package com.arbuzerxxl.vibeshot.data.storage.db.photo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arbuzerxxl.vibeshot.data.converters.ExifConverters
import com.arbuzerxxl.vibeshot.data.converters.TagsConverters
import com.arbuzerxxl.vibeshot.data.storage.db.photo.details.dao.DetailsDao
import com.arbuzerxxl.vibeshot.data.storage.db.photo.details.entities.DetailsPhotoEntity
import com.arbuzerxxl.vibeshot.data.storage.db.photo.interests.dao.InterestsDao
import com.arbuzerxxl.vibeshot.data.storage.db.photo.interests.entities.InterestsEntity

private const val DATABASE_VERSION = 1
const val DATABASE_NAME = "contraomnese_vibeshot_photos.db"

@TypeConverters(TagsConverters::class, ExifConverters::class)
@Database(
    entities = [InterestsEntity::class, DetailsPhotoEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun interestsDao(): InterestsDao
    abstract fun detailsDao(): DetailsDao

    companion object {
        fun create(context: Context): PhotoDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = PhotoDatabase::class.java,
                name = DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}