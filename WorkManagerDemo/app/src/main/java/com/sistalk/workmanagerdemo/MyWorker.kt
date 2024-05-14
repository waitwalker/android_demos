package com.sistalk.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(context: Context, workerParams: WorkerParameters) :Worker(context, workerParams) {
    private val tag = "MyWorker"
    override fun doWork(): Result {
        val name = inputData.getString(INPUT_DATA_KEY)
        Log.d(tag,"doWork:$name started")
        Thread.sleep(3000)
        Log.d(tag,"doWork:$name finished")
        return Result.success(workDataOf(OUTPUT_DATA_KEY to "$name output"))
    }
}