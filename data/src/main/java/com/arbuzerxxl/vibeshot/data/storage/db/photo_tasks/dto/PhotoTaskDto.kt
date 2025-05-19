package com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.dto

import androidx.room.ColumnInfo

data class PhotoTaskDto(

    @ColumnInfo(name = TASK) val task: String,

) {
    companion object {
        const val TASK = "alias_task"
    }
}