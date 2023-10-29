package com.arthur.commonlib.ability

import android.os.Handler
import android.os.Looper
import android.os.Message


/**
 *
 * created by guo.lei on 2022.04.12
 * 主线程执行器
 */
class MainThreadExecutor {
    companion object {
        private val handler by lazy {
            Handler(Looper.getMainLooper())
        }

        fun post(runnable: Runnable) {
            handler.post(runnable)
        }

        fun post(tag: Any, runnable: Runnable) {
            handler.sendMessage(Message.obtain(
                handler, runnable).apply {
                obj = getTag(tag)
            })
        }

        fun postAtFrontOfQueue(runnable: Runnable) {
            handler.postAtFrontOfQueue(runnable)
        }

        fun postDelayed(tag: Any, runnable: Runnable, delayMill: Long) {
            handler.sendMessageDelayed(Message.obtain(
                handler, runnable).apply {
                obj = getTag(tag)
            }, delayMill)
        }

        fun cancelRunnable(tag: Any, runnable: Runnable) {
            handler.removeCallbacks(runnable,
                getTag(tag)
            )
        }

        fun cancelRunnablesByTag(tag: Any) {
            handler.removeCallbacksAndMessages(
                getTag(tag)
            )
        }

        fun cancelAll() {
            handler.removeCallbacksAndMessages(null)
        }

        private fun getTag(tag: Any): Any {
            return if (tag is Number || tag is CharSequence) {
                tag.toString().intern()
            } else tag
        }
    }
}