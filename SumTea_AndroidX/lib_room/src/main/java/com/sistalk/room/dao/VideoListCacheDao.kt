package com.sistalk.room.dao

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface VideoListCacheDao {
    @Insert(entity = )
    suspend fun insert(videoInfo:VideoInfo)
}