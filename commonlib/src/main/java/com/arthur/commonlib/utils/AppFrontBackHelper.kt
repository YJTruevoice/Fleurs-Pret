package com.arthur.commonlib.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils

/**
 * description:
 *
 * @author donghaoguo  2020/2/18
 */
object AppFrontBackHelper {
    private val mOnAppStatusListeners: ArrayList<OnAppStatusListener> = ArrayList()
    private var lifecycleCallbackRegistered = false
    var isFirstOpen = true

    /**
     * 注册状态监听，仅在Application中使用
     * @param application
     * @param listener
     */
    fun register(application: Application, listener: OnAppStatusListener) {
        if (!mOnAppStatusListeners.contains(listener)) {
            mOnAppStatusListeners.add(listener)
        }
        if (!lifecycleCallbackRegistered) {
            application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
            lifecycleCallbackRegistered = true
        }
    }

    fun unRegister(listener: OnAppStatusListener) {
        mOnAppStatusListeners.remove(listener)
    }

    private val activityLifecycleCallbacks: ActivityLifecycleCallbacks =
        object : ActivityLifecycleCallbacks {
            //打开的Activity数量统计
            private var activityStartCount = 0
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                activityStartCount++
                //数值从0变到1说明是从后台切到前台
                if (activityStartCount == 1) {
                    //从后台切到前台
                    for (listener in mOnAppStatusListeners) {
                        listener.onFront()
                    }
                    if (isFirstOpen) {
                        isFirstOpen = false
                    }
                }
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                activityStartCount--
                //数值从1到0说明是从前台切到后台
                if (activityStartCount == 0) {
                    //从前台切到后台
                    for (listener in mOnAppStatusListeners) {
                        listener.onBack()
                    }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        }

    interface OnAppStatusListener {
        fun onFront()
        fun onBack()
    }

    /**
     * 判断应用是否是在后台
     */
    fun isBackground(context: Context): Boolean {
        val activityManager = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val keyguardManager =
            context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val appProcesses = activityManager
            .runningAppProcesses
        if (appProcesses != null) {
            for (appProcess in appProcesses) {
                if (TextUtils.equals(appProcess.processName, context.packageName)) {
                    val isBackground =
                        appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE
                    val isLockedState = keyguardManager.inKeyguardRestrictedInputMode()
                    return isBackground || isLockedState
                }
            }
        }
        return false
    }
}