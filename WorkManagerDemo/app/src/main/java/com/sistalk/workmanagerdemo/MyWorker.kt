package com.sistalk.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParams: WorkerParameters) :Worker(context, workerParams) {
    private val tag = "MyWorker"
    override fun doWork(): Result {
        Log.d(tag,"doWork:started")
        Thread.sleep(3000)
        Log.d(tag,"doWork:finished")
        return Result.success()
    }


}