package com.sistalk.roombasic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {

//    private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
//        override fun migrate(db: SupportSQLiteDatabase) {
//            db.execSQL("ALTER TABLE word ADD COLUMN bar_data INTEGER NOT NULL DEFAULT 0")
//        }
//    }

    companion object {
        private var instance: WordDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): WordDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordDatabase::class.java,
                    "word_database"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }

    abstract fun getWordDao(): WordDao

}





















