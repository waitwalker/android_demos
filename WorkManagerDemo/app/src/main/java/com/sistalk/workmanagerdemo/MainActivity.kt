package com.sistalk.workmanagerdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.setInputMerger
import androidx.work.workDataOf
import com.sistalk.workmanagerdemo.databinding.ActivityMainBinding

const val INPUT_DATA_KEY = "input_data_key"
const val OUTPUT_DATA_KEY = "output_data_key"
const val WORK_A_NAME = "WorkA"
class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"

    private lateinit var mainBinding:ActivityMainBinding

    // 创建worker 工作者
    private val workerManager = WorkManager.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        // setContentView(R.layout.activity_main)
        mainBinding.button.setOnClickListener {
            // 约束条件
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            // 工作请求，创建工作（可以是单次的或者周期性的）
            val workRequest:OneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setConstraints(constraints)
                .setInputData(workDataOf(INPUT_DATA_KEY to WORK_A_NAME))
                .build()
            workerManager.enqueue(workRequest)

            workerManager.getWorkInfoByIdLiveData(workRequest.id).observe(this, Observer {
                Log.d(tag,"state:${it?.state}")
                if (it?.state == WorkInfo.State.SUCCEEDED) {
                    Log.d(tag,"data:${it.outputData.getString(OUTPUT_DATA_KEY)}")
                }
            })
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}