package com.example.activitydemo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class FrameLayoutPageActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_layout)

        val frameButton:Button = findViewById(R.id.frame_id)
        frameButton.setOnClickListener {
            println("frame 单帧布局页上按钮被点击")
            finish()
        }
    }
}