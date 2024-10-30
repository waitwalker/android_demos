package com.sistalk.room.database

import androidx.room.Database
import com.sistalk.room.entity.VideoInfo

@Database(entities = [VideoInfo::class], version = 1, exportSchema = false)
abstract class SumDataBase {
    abstract fun videoListDao()
}