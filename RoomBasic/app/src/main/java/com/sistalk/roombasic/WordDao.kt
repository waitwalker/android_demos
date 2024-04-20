package com.sistalk.roombasic

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao // Database access object
interface WordDao {

    @Insert // 传递时可变参数
    fun insertWords(vararg words:Word)

    @Update // 可变参数的另一种写法
    fun updateWords(vararg words:Word)

    @Delete
    fun deleteWords(vararg words:Word)

    @Query("DELETE FROM WORD")
    fun deleteAllWords()

    @Query("SELECT * FROM WORD ORDER BY ID DESC")
    fun getAllWordsLive():LiveData<List<Word>>

}