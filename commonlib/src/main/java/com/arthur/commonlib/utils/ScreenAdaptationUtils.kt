package com.arthur.commonlib.utils

import android.app.Activity
import android.content.res.Resources

/**
 * 字节跳动屏幕适配方案，在创建视图前调用adapterScreen()，视图销毁前调用resetScreen()
 * 需与设计约定好设计稿采用的尺寸，默认为375dp
 */
object ScreenAdaptationUtils {

    fun adapterScreen(activity: Activity) {
        adapterScreen(activity, 375, false)
    }

    fun adapterScreen(activity: Activity, targetDP: Int, isVertical: Boolean) {
        //系统的屏幕尺寸
        val systemDM = Resources.getSystem().displayMetrics
        //app整体的屏幕尺寸
        val appDM = activity.application.resources.displayMetrics
        //activity的屏幕尺寸
        val activityDM = activity.resources.displayMetrics

        if (isVertical) {
            // 适配屏幕的高度
            activityDM.density = activityDM.heightPixels / targetDP.toFloat()
        } else {
            // 适配屏幕的宽度
            activityDM.density = activityDM.widthPixels / targetDP.toFloat()
        }
        // 适配相应比例的字体大小
        activityDM.scaledDensity = activityDM.density * (systemDM.scaledDensity / systemDM.density)
        // 适配dpi
        activityDM.densityDpi = (160 * activityDM.density).toInt()
    }

    fun resetScreen(activity: Activity) {
        //系统的屏幕尺寸
        val systemDM = Resources.getSystem().displayMetrics
        //app整体的屏幕尺寸
        val appDM = activity.application.resources.displayMetrics
        //activity的屏幕尺寸
        val activityDM = activity.resources.displayMetrics

        activityDM.density = systemDM.density
        activityDM.scaledDensity = systemDM.scaledDensity
        activityDM.densityDpi = systemDM.densityDpi

        appDM.density = systemDM.density
        appDM.scaledDensity = systemDM.scaledDensity
        appDM.densityDpi = systemDM.densityDpi
    }

}