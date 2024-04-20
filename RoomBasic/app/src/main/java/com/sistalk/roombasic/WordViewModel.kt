package com.sistalk.roombasic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class WordViewModel(application: Application):AndroidViewModel(application) {
    private var wordRepository = WordRepository(application)

    fun getAllWordsLive():LiveData<List<Word>> {
        return wordRepository.getAllWordsLive()
    }


    /// 插入数据 通过协程方式
    fun insertWords(vararg words:Word) {
//        wordDao.insertWords(words = words)
//        InsertAsyncTask(wordDao).execute(*words)
        wordRepository.insertWords(*words)
    }

    fun updateWords(vararg words:Word) {
//        wordDao.updateWords(words = words)
//        UpdateAsyncTask(wordDao).execute(*words)
        wordRepository.updateWords(*words)
    }

    fun deleteWords(vararg word: Word) {
//        wordDao.deleteWords(words = word)
//        DeleteAsyncTask(wordDao).execute(*word)
        wordRepository.deleteWords(*word)
    }

    fun deleteAllWords() {
//        wordDao.deleteAllWords()
        //DeleteAllAsyncTask(wordDao).execute()
        wordRepository.deleteAllWords()
    }


}