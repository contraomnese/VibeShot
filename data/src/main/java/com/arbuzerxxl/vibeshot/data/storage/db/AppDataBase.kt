package com.arbuzerxxl.vibeshot.data.storage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arbuzerxxl.vibeshot.data.converters.ExifConverters
import com.arbuzerxxl.vibeshot.data.converters.TagsConverters
import com.arbuzerxxl.vibeshot.data.storage.db.details.dao.DetailsDao
import com.arbuzerxxl.vibeshot.data.storage.db.details.entities.DetailsPhotoEntity
import com.arbuzerxxl.vibeshot.data.storage.db.interests.dao.InterestsDao
import com.arbuzerxxl.vibeshot.data.storage.db.interests.entities.InterestsEntity

private const val DATABASE_VERSION = 1
const val DATABASE_NAME = "contraomnese_vibeshot.db"

@TypeConverters(TagsConverters::class, ExifConverters::class)
@Database(
    entities = [InterestsEntity::class, DetailsPhotoEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun interestsDao(): InterestsDao
    abstract fun detailsDao(): DetailsDao

    companion object {
        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}