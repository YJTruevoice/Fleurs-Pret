package com.arthur.commonlib.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class ValuesUtils {

    companion object {
        private val application by lazy {
            com.arthur.commonlib.ability.AppKit.context
        }

        /**
         * 获取资源字符串
         */
        fun getString(@StringRes id: Int): String {
            return application.getString(id)
        }

        fun getFormatString(@StringRes id: Int, vararg objects: Any): String {
            return application.getString(id, *objects)
        }

        fun getAppendString(@StringRes id: Int, vararg strings: String): String {
            val stringBuilder = StringBuilder(application.getString(id))
            for (string in strings) {
                stringBuilder.append(string)
            }
            return stringBuilder.toString()
        }

        fun getAppendString(vararg ids: Int): String {
            val stringBuilder = StringBuilder()
            for (id in ids) {
                stringBuilder.append(application.getString(id))
            }
            return stringBuilder.toString()
        }

        fun getAppendString(vararg strings: String?): String {
            val stringBuilder = StringBuilder()
            for (string in strings) {
                stringBuilder.append(string)
            }
            return stringBuilder.toString()
        }

        /**
         * 获取颜色值
         */
        fun getColor(@ColorRes id: Int): Int {
            return ContextCompat.getColor(application, id)
        }

        /**
         * 针对Activity设置深色模式需要用对应的Activity Context获取资源
         */
        fun getColor(@ColorRes id: Int, context: Context): Int {
            return ContextCompat.getColor(context, id)
        }

        /**
         * 获取drawable
         */
        fun getDrawableById(@DrawableRes id: Int): Drawable? {
            return ContextCompat.getDrawable(application, id)
        }

        fun getDrawableById(@DrawableRes id: Int, context: Context): Drawable? {
            return ContextCompat.getDrawable(context, id)
        }
    }
}