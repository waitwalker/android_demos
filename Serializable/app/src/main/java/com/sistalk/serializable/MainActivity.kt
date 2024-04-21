package com.sistalk.serializable

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    val FILE_NAME:String = "My_DATA_FILE"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.save).setOnClickListener(View.OnClickListener {
            val score = Score(100,96,99)
            val student = Student("zhangsan",20,score)

            try {
                val objectOutStream = ObjectOutputStream(openFileOutput(FILE_NAME, MODE_PRIVATE))
                objectOutStream.writeObject(student)
                objectOutStream.flush()
                objectOutStream.close()
                Toast.makeText(this,"Data Saved!",Toast.LENGTH_LONG).show()
            } catch (e:Exception) {
                Log.e("activity","e=$e")
            }
        })

        findViewById<Button>(R.id.load).setOnClickListener(View.OnClickListener {
            try {
                val objectInputStream = ObjectInputStream(openFileInput(FILE_NAME))
                val student: Student = objectInputStream.readObject() as Student
                Log.w("activity","student ${student.name} \nage=${student.age} \nmath=${student.score.math} \nchinese=${student.score.chinese} \nenglish=${student.score.english}")
            } catch (e:Exception) {
                Log.e("activity","e=$e")
            }
        })
    }
}