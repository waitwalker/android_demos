package com.sistalk.roombasic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Word(
    @ColumnInfo(name = "english_word") var word: String, @ColumnInfo(name = "chinese_meaning") var chineseMeaning: String
) {
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

}