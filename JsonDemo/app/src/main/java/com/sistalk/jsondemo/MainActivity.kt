package com.sistalk.jsondemo

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val student = Student("zhangsan",20, Score(100))
        val student2 = Student("zhangsan2",22, Score(100))
        val students = ArrayList<Student>()
        students.add(student)
        students.add(student2)
        val jsonString = Gson().toJson(students)
        Log.w("MainActivity","已经将对象转为json string=$jsonString")
        val type:Type = object : TypeToken<List<Student>>(){}.type
        val student1s = Gson().fromJson<List<Student>>(jsonString,type)
        Log.e("MainActivity", "转换完的结果student=${student1s[0].name}\nage=${student1s[0].age}\n")


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}