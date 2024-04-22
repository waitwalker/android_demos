package com.sistalk.parcelable

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.save).setOnClickListener(View.OnClickListener {
            val intent = Intent(this,MainActivity2::class.java)
            val bundle = Bundle()
            val student = Student("张三",20,Score(100,100,99))
            bundle.putParcelable("student",student)
            intent.putExtra("data",bundle)
            startActivity(intent)
        })

        findViewById<Button>(R.id.load).setOnClickListener(View.OnClickListener {

        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}