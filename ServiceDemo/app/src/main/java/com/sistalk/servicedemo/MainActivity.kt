package com.sistalk.servicedemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.sistalk.servicedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val tag = "Hello"

    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(tag, "onCreate:MainActivity")
        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewBinding.button.setOnClickListener {
            Intent(this, MyService::class.java).also {
                //intent还可以放一些参数
                startService(it)
            }
        }

        viewBinding.bindService.setOnClickListener {
            val bindIntent = Intent(this, MyService::class.java)
            val serviceConnection = object : ServiceConnection {
                // 取消绑定
                override fun onServiceDisconnected(name: ComponentName?) {

                }

                // 绑定成功
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    //  name服务名称 service可供访问的服务
                    (service as MyService.MyBinder).service.numberLiveData.observe(
                        this@MainActivity
                    ) {
                        viewBinding.textView.text = "$it"
                    }
                }


            }
            startService(bindIntent)
            bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(tag, "onStart:MainActivity")
    }

    override fun onStop() {
        super.onStop()
        Log.i(tag, "onStop:MainActivity")
//        Intent(this,MyService::class.java).also {
//            stopService(it)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(tag, "onDestroy:MainActivity")
    }


}