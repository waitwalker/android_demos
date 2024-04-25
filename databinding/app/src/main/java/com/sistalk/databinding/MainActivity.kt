package com.sistalk.databinding

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import com.sistalk.databinding.app.MyApplication
import com.sistalk.databinding.databinding.ActivityMainBinding
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private lateinit var myViewModel: MyAndroidViewModel
    lateinit var binding: ActivityMainBinding

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val application:MyApplication = requireNotNull(this).application as MyApplication
        val factory = MyViewModelFactory(application, SavedStateHandle())

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        myViewModel = ViewModelProvider(this, factory)[MyAndroidViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.viewModel = myViewModel
        binding.lifecycleOwner = this

        val shp: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = shp.edit()
        editor.putInt("number", 100)
        editor.apply()

        val value: Int = shp.getInt("number", 0)
        println("value=$value")

        val data: MyData = MyData(applicationContext)//不能传递this
        data.save(100)

        data.load()




        Timer().schedule(MyTimerTask(), 1000)
    }

    override fun onPause() {
        super.onPause()
    }
}

class MyTimerTask : TimerTask() {
    override fun run() {

//        println("MyApplication().applicationContext=${MyApplication().appContext()}")
    }

}