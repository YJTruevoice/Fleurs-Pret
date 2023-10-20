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
            com.arthur.commonlib.ability.MainThreadExecutor.Companion.handler.post(runnable)
        }

        fun post(tag: Any, runnable: Runnable) {
            com.arthur.commonlib.ability.MainThreadExecutor.Companion.handler.sendMessage(Message.obtain(
                com.arthur.commonlib.ability.MainThreadExecutor.Companion.handler, runnable).apply {
                obj = com.arthur.commonlib.ability.MainThreadExecutor.Companion.getTag(tag)
            })
        }

        fun postAtFrontOfQueue(runnable: Runnable) {
            com.arthur.commonlib.ability.MainThreadExecutor.Companion.handler.postAtFrontOfQueue(runnable)
        }

        fun postDelayed(tag: Any, runnable: Runnable, delayMill: Long) {
            com.arthur.commonlib.ability.MainThreadExecutor.Companion.handler.sendMessageDelayed(Message.obtain(
                com.arthur.commonlib.ability.MainThreadExecutor.Companion.handler, runnable).apply {
                obj = com.arthur.commonlib.ability.MainThreadExecutor.Companion.getTag(tag)
            }, delayMill)
        }

        fun cancelRunnable(tag: Any, runnable: Runnable) {
            com.arthur.commonlib.ability.MainThreadExecutor.Companion.handler.removeCallbacks(runnable,
                com.arthur.commonlib.ability.MainThreadExecutor.Companion.getTag(tag)
            )
        }

        fun cancelRunnablesByTag(tag: Any) {
            com.arthur.commonlib.ability.MainThreadExecutor.Companion.handler.removeCallbacksAndMessages(
                com.arthur.commonlib.ability.MainThreadExecutor.Companion.getTag(tag)
            )
        }

        fun cancelAll() {
            com.arthur.commonlib.ability.MainThreadExecutor.Companion.handler.removeCallbacksAndMessages(null)
        }

        private fun getTag(tag: Any): Any {
            return if (tag is Number || tag is CharSequence) {
                tag.toString().intern()
            } else tag
        }
    }
}