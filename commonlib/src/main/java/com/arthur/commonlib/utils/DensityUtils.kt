package com.arthur.commonlib.utils

import android.content.Context
import com.arthur.commonlib.ability.AppKit

class DensityUtils {
    companion object {
        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        fun dp2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        fun px2dp(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        /**
         * sp转px
         */
        fun sp2px(context: Context, spValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        /**
         * px转sp
         */
        fun px2sp(context: Context, pxValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }

        fun Float.dp2px(context: Context?): Int {
            return dp2px(context?: AppKit.context, this)
        }
        fun Float.px2dp(context: Context?): Int {
            return px2dp(context?: AppKit.context, this)
        }
        fun Float.sp2px(context: Context?): Int {
            return sp2px(context?: AppKit.context, this)
        }
        fun Float.px2sp(context: Context?): Int {
            return px2sp(context?: AppKit.context, this)
        }

    }
}