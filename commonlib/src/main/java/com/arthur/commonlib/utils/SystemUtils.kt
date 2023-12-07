package com.arthur.commonlib.utils

import android.animation.ValueAnimator
import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Process
import android.os.Vibrator
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.arthur.commonlib.ability.AppKit
import java.io.File
import java.lang.reflect.Method
import kotlin.system.exitProcess


/**
 * 与系统相关的一些方法
 */
class SystemUtils {

    companion object {
        /**
         * 模拟器上特有的几个文件，用以判断是否为模拟器
         */
        private val known_files = arrayOf(
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props"
        )

        /**
         * 显示软键盘
         */
        fun showKeyboard(context: Context?) {
            val activity = context as Activity?
            if (activity != null) {
                val imm = activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (activity.currentFocus != null) {
                    imm.showSoftInputFromInputMethod(
                        activity.currentFocus!!
                            .windowToken, 0
                    )
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }
        }

        /**
         * 隐藏软键盘
         */
        fun hideKeyboard(context: Context?) {
            val activity = context as Activity?
            if (activity != null) {
                val imm = activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isActive && activity.currentFocus != null) {
                    imm.hideSoftInputFromWindow(
                        activity.currentFocus!!
                            .windowToken, 0
                    )
                }
            }
        }

        /**
         * 杀进程
         */
        fun killAppProcess() {
            //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
            val mActivityManager =
                AppKit.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val mList = mActivityManager.runningAppProcesses
            for (runningAppProcessInfo in mList) {
                if (runningAppProcessInfo.pid != Process.myPid()) {
                    Process.killProcess(runningAppProcessInfo.pid)
                }
            }
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }

        /**
         * 设置背景透明度
         */
        fun setBackgroundAlpha(activity: Activity, dark: Boolean) {
            setBackgroundAlpha(activity, if (dark) 0.5f else 1f)
        }

        fun setBackgroundAlpha(activity: Activity, alpha: Float, duration: Long = 300L) {
            val layoutParams = activity.window.attributes
            val animator = ValueAnimator.ofFloat(layoutParams.alpha, alpha)
            animator.duration = duration
            animator.addUpdateListener { animation: ValueAnimator ->
                layoutParams.alpha = animation.animatedValue as Float
                activity.window.attributes = layoutParams
            }
            animator.start()
            if (alpha < 1) {
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
        }

        /**
         * 判断是否是emui
         */
        fun isEmui(): Boolean {
            return !getSystemProperty("ro.build.version.emui", null).isNullOrEmpty()
        }

        /**
         * 判断是否为鸿蒙系统
         */
        fun isHarmonyOS(): Boolean {
            try {
                val clz = Class.forName("com.huawei.system.BuildEx")
                val method: Method = clz.getMethod("getOsBrand")
                return "harmony" == method.invoke(clz)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        /**
         * 判断当前是否为模拟器
         */
        fun isEmulator(): Boolean {
            //检测模拟器上特有的几个文件
            for (i in known_files.indices) {
                val file_name: String = known_files.get(i)
                val qemu_file = File(file_name)
                if (qemu_file.exists()) {
                    return true
                }
            }

            //判断cpu支持x86
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val abi = Build.SUPPORTED_ABIS
                return abi != null && abi.isNotEmpty() && abi[0].contains("x86")
            }
            return false
        }

        private fun getSystemProperty(key: String, defaultValue: String?): String? {
            try {
                val clz = Class.forName("android.os.SystemProperties")
                val get = clz.getMethod("get", String::class.java, String::class.java)
                return get.invoke(clz, key, defaultValue) as String
            } catch (e: java.lang.Exception) {
            }
            return defaultValue
        }

        /**
         *  判断软件是否已安装
         *  @param packageName 包名
         *  @return true:已安装
         */
        fun isAppInstalled(context: Context, packageName: String): Boolean {
            val info: PackageInfo?
            val packageManager = context.packageManager
            info = try {
                packageManager.getPackageInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }

            return info != null
        }

        /**
         * 获取导航栏高度
         */
        fun getNavigationBarHeight(context: Context): Int {
            val rid: Int =
                context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
            return if (rid != 0) {
                val resourceId: Int =
                    context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
                context.resources.getDimensionPixelSize(resourceId)
            } else {
                0
            }
        }

        /**
         * 震动
         */
        fun doVibrator(time: Long = 50) {
            val vibrator = AppKit.context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(time)
        }

        fun isWifiConnected(context: Context): Boolean {
            val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            return wifiNetworkInfo?.isConnected?: false
        }
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (connectivityManager != null) {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
            return false
        }

    }

}