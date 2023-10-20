package com.arthur.commonlib.utils

class GestureUtils {

    companion object {

        /**
         * 记录上次点击时间，用于避免连点操作
         */
        private var lastClickTime: Long = 0L

        /**
         * 避免重复点击
         */
        fun isFastDoubleClick(): Boolean {
            val time = System.currentTimeMillis()
            val timeD: Long = time - lastClickTime
            if (timeD in 1..999) {
                return true
            }
            lastClickTime = time
            return false
        }

        fun isFastDoubleClick(duration: Long): Boolean {
            val time = System.currentTimeMillis()
            val timeD: Long = time - lastClickTime
            if (timeD in 1 until duration) {
                return true
            }
            lastClickTime = time
            return false
        }

    }

}