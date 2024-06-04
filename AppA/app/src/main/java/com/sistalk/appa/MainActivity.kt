package com.sistalk.appa

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sistalk.appa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        //setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 隐式intent跳转
        mainBinding.jumpButton.setOnClickListener {
            // 隐式跳转
            val mIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://waitwalker.cn"))
            if (mIntent.resolveActivity(packageManager) != null) {
                startActivity(mIntent)
            } else {
                Toast.makeText(this,"App 没有安装，不能处理跳转", Toast.LENGTH_LONG).show()
            }
        }

        // 显示intent跳转
        mainBinding.button2.setOnClickListener {
            val mIntent = Intent()
            mIntent.component = ComponentName("com.sistalk.appb","com.sistalk.appb.SecondActivity")
            if (mIntent.resolveActivity(packageManager) != null) {
                startActivity(mIntent);
            } else {
                Toast.makeText(this, "App B is not installed.", Toast.LENGTH_LONG).show();
            }
        }

        // 通过package manager 涉及到隐私权限
        mainBinding.buttonPackage.setOnClickListener {
            val packageName = "com.sistalk.appc"
            if (isAppInstalled(packageName)) {
                val mIntent = packageManager.getLaunchIntentForPackage(packageName)
                if (mIntent != null) {
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(mIntent)
                }
            } else {
                Toast.makeText(this,"$packageName 没有安装",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isAppInstalled(packageName:String): Boolean{
        var installed = false
        try {
            val packageInfo = packageManager.getPackageInfo(packageName,PackageManager.GET_ACTIVITIES)
            if (packageInfo.packageName == packageName) {
                installed = true
            }
        } catch (e:Exception) {
            installed = false
        } finally {
            Log.d("MainActivity","应用安装状态:$installed")
        }
        return installed
    }
}