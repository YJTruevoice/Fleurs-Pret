package com.arthur.commonlib.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import java.util.*

object ActivityManager : Application.ActivityLifecycleCallbacks {

    private val mActivities = LinkedList<Activity>()

    /**
     * 获取当前栈顶activity
     */
    fun getCurrentActivity(): Activity? {
        return mActivities.peekFirst()
    }

    /**
     * 从栈顶找第一个未销毁Activity
     * @param reliable 是否可靠（非销毁且可见）
     */
    fun getTopActivity(reliable: Boolean): Activity? {
        if (mActivities.isEmpty()) {
            return null
        }
        val atLeastState = if (reliable) Lifecycle.State.STARTED else Lifecycle.State.CREATED
        val rit = mActivities.listIterator(mActivities.size)
        while (rit.hasPrevious()) {
            val ac = rit.previous()
            if (ac is AppCompatActivity && ac.lifecycle.currentState.isAtLeast(atLeastState) && !ac.isFinishing && !ac.isDestroyed) {
                return ac
            }
        }
        return null
    }

    /**
     * 结束栈顶activity
     */
    fun finishCurrentActivity() {
        getCurrentActivity()?.finish()
    }

    /**
     * 结束指定activity
     */
    fun finishActivity(activity: Activity) {
        if (mActivities.isEmpty()) {
            return
        }
        mActivities.remove(activity)
        activity.finish()
    }

    /**
     * 结束指定类名activity
     */
    fun finishActivity(cls: Class<out Activity>) {
        if (mActivities.isEmpty()) {
            return
        }
        for (activity in mActivities) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 从栈顶找到第一个指定类名Activity
     */
    fun getActivityByClassFromTop(cls: Class<out Activity>): Activity? {
        if (mActivities.isEmpty()) {
            return null
        }
        val rit = mActivities.listIterator(mActivities.size)
        while (rit.hasPrevious()) {
            val ac = rit.previous()
            if (ac.javaClass == cls) {
                return ac
            }
        }
        return null
    }

    /**
     * 从栈底找到第一个指定类名Activity
     */
    fun getActivityByClassFromBottom(cls: Class<out Activity>): Activity? {
        if (mActivities.isEmpty()) {
            return null
        }
        val it = mActivities.listIterator(mActivities.size)
        while (it.hasNext()) {
            val ac = it.next()
            if (ac.javaClass == cls) {
                return ac
            }
        }
        return null
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        mActivities.push(activity)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        if (mActivities.isEmpty()) {
            return
        }
        if (mActivities.contains(activity)) {
            mActivities.remove(activity)
        }
    }

}