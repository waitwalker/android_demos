package com.sistalk.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "table_video_list")
data class VideoInfo(
    // 主键
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "title", defaultValue = "")
    var title: String?,

    @ColumnInfo(name = "desc")
    var desc: String?,

    @ColumnInfo(name = "authorName")
    var authorName:String?,

    @ColumnInfo(name = "playUrl")
    var playUrl: String?,

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String?,

    @Ignore
    var collectionCount:String?
) : Parcelable {

}