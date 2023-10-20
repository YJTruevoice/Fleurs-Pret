package com.arthur.commonlib.ability

import android.annotation.SuppressLint
import android.content.Context

/**
 *
 * created by guo.lei
 * on 2022.03.31
 */
class AppKit {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context : Context

        fun initContext(ctx : Context) {
            context = ctx.applicationContext
        }
    }
}