package com.sistalk.roombasic

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class WordRepository(private val context: Context) {

    private var wordDao: WordDao = WordDatabase.getDatabase(context.applicationContext).getWordDao()
    private var allWordsLive: LiveData<List<Word>> = wordDao.getAllWordsLive()

    fun getAllWordsLive():LiveData<List<Word>> {
        return allWordsLive
    }

    /// 插入数据 通过协程方式
    fun insertWords(vararg words:Word) {
//        wordDao.insertWords(words = words)
        InsertAsyncTask(wordDao).execute(*words)
    }

    fun updateWords(vararg words:Word) {
//        wordDao.updateWords(words = words)
        UpdateAsyncTask(wordDao).execute(*words)
    }

    fun deleteWords(vararg word: Word) {
//        wordDao.deleteWords(words = word)
        DeleteAsyncTask(wordDao).execute(*word)
    }

    fun deleteAllWords() {
//        wordDao.deleteAllWords()
        DeleteAllAsyncTask(wordDao).execute()
    }

    class InsertAsyncTask(private val wordDao: WordDao): AsyncTask<Word, Any, Any>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg words: Word) {
            wordDao.insertWords(words = words)
        }
    }

    class UpdateAsyncTask(private val wordDao: WordDao): AsyncTask<Word, Any, Any>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg words: Word) {
            wordDao.updateWords(words = words)
        }
    }

    class DeleteAsyncTask(private val wordDao: WordDao): AsyncTask<Word, Any, Any>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg words: Word) {
            wordDao.deleteWords(words = words)
        }
    }

    class DeleteAllAsyncTask(private val wordDao: WordDao): AsyncTask<Any, Any, Any>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Any?) {
            wordDao.deleteAllWords()
        }
    }
}