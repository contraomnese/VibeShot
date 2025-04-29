package com.arbuzerxxl.vibeshot.data.storage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arbuzerxxl.vibeshot.data.storage.db.interests.dao.InterestsDao
import com.arbuzerxxl.vibeshot.data.storage.db.interests.entities.InterestsEntity

private const val DATABASE_VERSION = 1
const val DATABASE_NAME = "contraomnese_vibeshot.db"

@Database(
    entities = [InterestsEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun interestsDao(): InterestsDao

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