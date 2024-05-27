package com.sistalk.servicedemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sistalk.servicedemo.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var viewActivitySecondBinding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewActivitySecondBinding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(viewActivitySecondBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        viewActivitySecondBinding.startBind.setOnClickListener {
            // 测试服务一直存在
            val bindIntent = Intent(this, MyService::class.java)
            val serviceConnection = object : ServiceConnection {
                // 取消绑定
                override fun onServiceDisconnected(name: ComponentName?) {

                }

                // 绑定成功
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    //  name服务名称 service可供访问的服务
                    (service as MyService.MyBinder).service.numberLiveData.observe(
                        this@SecondActivity
                    ) {
                        viewActivitySecondBinding.textView2.text = "$it"
                    }
                }


            }
            bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }
}