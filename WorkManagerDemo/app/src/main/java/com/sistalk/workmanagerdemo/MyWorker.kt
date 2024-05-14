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
        Thread.sleep(6000)
        val sp = applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        var number = sp.getInt(name,0)
        sp.edit().putInt(name,++number).apply()
        Log.d(tag,"doWork:$name finished")
        return Result.success(workDataOf(OUTPUT_DATA_KEY to "$name output"))
    }
}