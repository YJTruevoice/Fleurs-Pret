package com.facile.immediate.electronique.fleurs.pret.common

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

/**
 * 封装一个利用MainLooper创建的Handler，方便在主线程进行操作
 */
object MainThread {

    private val mainHandler = Handler(Looper.getMainLooper())

    fun post(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            mainHandler.post(runnable)
        }
    }

    fun postDelay(runnable: Runnable, delayMillis: Long) {
        mainHandler.postDelayed(runnable, delayMillis)
    }

    fun remove(runnable: Runnable?) {
        runnable?.let {
            mainHandler.removeCallbacks(it)
        }
    }

}


object WorkThread {
    private val workThread = HandlerThread("work_thread")
    private val workHandler: Handler

    init {
        workThread.start()
        workHandler = Handler(workThread.looper)
    }

    fun post(runnable: Runnable) {
        if (Looper.myLooper() == workThread.looper) {
            runnable.run()
        } else {
            workHandler.post(runnable)
        }
    }

    fun postDelay(runnable: Runnable, delayMillis: Long) {
        workHandler.postDelayed(runnable, delayMillis)
    }

    fun removeCallbacks(runnable: Runnable) {
        workHandler.removeCallbacks(runnable)
    }
}