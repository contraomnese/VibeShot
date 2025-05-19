package com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.dao.PhotoTasksDao
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.dao.TasksCategoryDao
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.entities.MoodEntity
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.entities.PhotoTaskEntity
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.entities.SeasonEntity
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.entities.TopicEntity

private const val DATABASE_VERSION = 1
const val DATABASE_NAME = "contraomnese_vibeshot_photo_tasks.db"

@Database(
    entities = [PhotoTaskEntity::class, MoodEntity::class, SeasonEntity::class, TopicEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class PhotoTasksDatabase : RoomDatabase() {

    abstract fun tasksDao(): PhotoTasksDao
    abstract fun tasksCategoryDao(): TasksCategoryDao

    companion object {
        fun create(context: Context): PhotoTasksDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = PhotoTasksDatabase::class.java,
                name = DATABASE_NAME
            )
                .createFromAsset(DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}