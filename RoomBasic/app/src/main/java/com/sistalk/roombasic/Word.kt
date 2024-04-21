package com.sistalk.roombasic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Word(
    @ColumnInfo(name = "english_word") var word: String,
    @ColumnInfo(name = "chinese_meaning") var chineseMeaning: String,
    @ColumnInfo(name = "foo_date") var foo:Boolean = false,
    @ColumnInfo(name = "bar_data") var bar:Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}