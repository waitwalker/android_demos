package com.sistalk.roombasic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {
    companion object {
        private var instance: WordDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): WordDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordDatabase::class.java,
                    "word_database"
                ).build()
            }
            return instance!!
        }
    }

    abstract fun getWordDao(): WordDao
}