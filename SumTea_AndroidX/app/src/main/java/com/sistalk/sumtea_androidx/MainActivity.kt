package com.sistalk.sumtea_androidx
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sistalk.framework.log.LogUtil
import com.sistalk.sumtea_androidx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        handleMainMessage()

        mainBinding.mainSend.setOnClickListener {
            sendToMain()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun handleMainMessage() {
        mainHandler = Handler(mainLooper) {
            LogUtil.d("获取的消息：${it.obj}")
            mainBinding.textView.text = "${it.obj}"
            false
        }
    }

    private fun sendToMain() {
        Thread(kotlinx.coroutines.Runnable {
            run {
                val result = performComplexCalculation()
                val message = mainHandler.obtainMessage(1,result)
                mainHandler.sendMessage(message)
            }
        }).start()
    }

    private fun performComplexCalculation():Int {
        try {
            Thread.sleep(2000)
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return 42
    }
}
