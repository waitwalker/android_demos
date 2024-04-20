package com.sistalk.roombasic

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
//    private lateinit var wordDatabase: WordDatabase
//    private lateinit var wordDao: WordDao
    private lateinit var textView: TextView
    private lateinit var buttonInsert:Button
    private lateinit var buttonUpdate:Button
    private lateinit var buttonClear:Button
    private lateinit var buttonDelete:Button
//    private lateinit var allWordsLive:LiveData<List<Word>>
    private lateinit var wordViewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        wordViewModel = ViewModelProvider(this)[WordViewModel::class.java]

//        wordDatabase = Room.databaseBuilder(this, WordDatabase::class.java, "word_database").allowMainThreadQueries().build()
//        wordDao = wordDatabase.getWordDao()
//        allWordsLive = wordDao.getAllWordsLive()
        textView = findViewById(R.id.textView)
        buttonInsert = findViewById(R.id.buttonInsert)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonClear = findViewById(R.id.buttonClear)
        buttonDelete = findViewById(R.id.buttonDelete)
        this.buttonInsert.setOnClickListener {
            val word1 = Word("Hello", "你好！")
            val word2 = Word("World", "世界！")
//            wordDao.insertWords(word1, word2)
//            InsertAsyncTask(wordDao).execute(word1,word2)
            wordViewModel.insertWords(word1,word2)
        }
        this.buttonClear.setOnClickListener {
//            wordDao.deleteAllWords()
//            DeleteAllAsyncTask(wordDao).execute()
            wordViewModel.deleteAllWords()
        }
        buttonUpdate.setOnClickListener{
            val word = Word("Hi","你好啊")
            word.id = 103
//            wordDao.updateWords(word)
//            UpdateAsyncTask(wordDao).execute(word)
            wordViewModel.updateWords(word)
        }

        buttonDelete.setOnClickListener{
            val word = Word("Hi","你好啊")
            word.id = 134
//            wordDao.deleteWords(word)
//            DeleteAsyncTask(wordDao).execute(word)
            wordViewModel.deleteWords(word)
        }

        wordViewModel.getAllWordsLive().observe(this) {

            var text = ""
            for (i in 0..<it.count()) {
                val word: Word = it[i]
                text += "${word.id}:${word.word}=${word.chineseMeaning}\n"
            }
            textView.text = text
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}