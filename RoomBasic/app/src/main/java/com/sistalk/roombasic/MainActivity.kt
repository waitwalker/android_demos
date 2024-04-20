package com.sistalk.roombasic

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var buttonInsert:Button
    private lateinit var buttonUpdate:Button
    private lateinit var buttonClear:Button
    private lateinit var buttonDelete:Button
    private lateinit var wordViewModel: WordViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    private lateinit var myAdapter2: MyAdapter
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switch: Switch

    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycleView)
        myAdapter = MyAdapter(false)
        myAdapter2 = MyAdapter(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        switch = findViewById(R.id.switch1)
        switch.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                recyclerView.adapter = myAdapter2
            } else {
                recyclerView.adapter = myAdapter
            }
        }
        recyclerView.adapter = myAdapter
        wordViewModel = ViewModelProvider(this)[WordViewModel::class.java]
        buttonInsert = findViewById(R.id.buttonInsert)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonClear = findViewById(R.id.buttonClear)
        buttonDelete = findViewById(R.id.buttonDelete)
        this.buttonInsert.setOnClickListener {
            val word1 = Word("Hello", "你好！")
            val word2 = Word("World", "世界！")
            wordViewModel.insertWords(word1,word2)
        }
        this.buttonClear.setOnClickListener {
            wordViewModel.deleteAllWords()
        }
        buttonUpdate.setOnClickListener{
            val word = Word("Hi","你好啊")
            word.id = 103
            wordViewModel.updateWords(word)
        }

        buttonDelete.setOnClickListener{
            val word = Word("Hi","你好啊")
            word.id = 134
            wordViewModel.deleteWords(word)
        }

        wordViewModel.getAllWordsLive().observe(this) {
            myAdapter.setAllWords(words = it)
            myAdapter2.setAllWords(words = it)
            // 告诉recycleView通知数据变化了，刷新视图
            myAdapter.notifyDataSetChanged()
            myAdapter2.notifyDataSetChanged()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}