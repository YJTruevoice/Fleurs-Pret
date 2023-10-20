package com.arthur.commonlib.ability

import android.util.Log

object Logger {

    var defaultTag = "Logger"

    fun logV(message: String) {
        logV(
            defaultTag,
            message
        )
    }
    fun logD(message: String) {
        logD(
            defaultTag,
            message
        )
    }
    fun logI(message: String) {
        logI(
            defaultTag,
            message
        )
    }
    fun logW(message: String) {
        logW(
            defaultTag,
            message
        )
    }
    fun logE(message: String) {
        logE(
            defaultTag,
            message
        )
    }

    fun logV(tag: String, message: String) {
        Log.v(tag, message)
    }
    fun logD(tag: String, message: String) {
        Log.d(tag, message)
    }
    fun logI(tag: String, message: String) {
        Log.i(tag, message)
    }
    fun logW(tag: String, message: String) {
        Log.w(tag, message)
    }
    fun logE(tag: String, message: String) {
        Log.e(tag, message)
    }

}