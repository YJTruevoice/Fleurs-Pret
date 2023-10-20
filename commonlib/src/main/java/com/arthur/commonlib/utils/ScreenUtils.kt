package com.arthur.commonlib.utils

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowInsets
import android.view.WindowManager

class ScreenUtils {

    companion object {
        /**
         * 获取屏幕宽度 *
         */
        fun getScreenWidth(context: Context): Int {
            return getDisplayMetrics(context)?.widthPixels ?: 0
        }

        /**
         * 获取屏幕高度 *
         */
        fun getScreenHeight(context: Context): Int {
            return getDisplayMetrics(context)?.heightPixels ?: 0
        }

        fun getDisplayMetrics(context: Context): DisplayMetrics? {
            return context.resources.displayMetrics
        }

        /**
         * 获取屏幕实际显示的区域大小
         */
        fun windowSize(context: Context): Size {
            val windowWidth: Int
            val windowHeight: Int
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = wm.currentWindowMetrics
                val windowInsets = windowMetrics.windowInsets
                val insets = windowInsets.getInsetsIgnoringVisibility(
                    WindowInsets.Type.navigationBars()
                            or WindowInsets.Type.displayCutout()
                )
                val insetsWidth = insets.right + insets.left
                val insetsHeight = insets.top + insets.bottom

                // Legacy size that Display#getSize reports
                val bounds = windowMetrics.bounds
                val legacySize = Size(
                    bounds.width() - insetsWidth,
                    bounds.height() - insetsHeight
                )
                windowWidth = legacySize.width
                windowHeight = legacySize.height
            } else {
                val displayMetrics = DisplayMetrics()
                wm.defaultDisplay.getMetrics(displayMetrics)
                windowWidth = displayMetrics.widthPixels
                windowHeight = displayMetrics.heightPixels
            }
            return Size(windowWidth, windowHeight)
        }
    }

}