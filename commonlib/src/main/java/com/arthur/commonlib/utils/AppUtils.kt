package com.arthur.commonlib.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import java.lang.Exception

/**
 * APP相关信息工具类
 */
class AppUtils {

    companion object {

        /**
         * 是否可调试状态
         */
        fun isDebuggable(): Boolean {
            return try {
                (com.arthur.commonlib.ability.AppKit.context.applicationInfo?.flags?: 0) and ApplicationInfo.FLAG_DEBUGGABLE != 0
            } catch (e: Exception) {
                false
            }
        }

        /**
         * 获取app版本名称
         *
         * @param context 上下文
         * @return app版本名称
         */
        fun getAppVersionName(context: Context): String {
            var versionName = ""
            try {
                val pm = context.packageManager
                val packageInfo = pm.getPackageInfo(context.packageName, 0)
                versionName = packageInfo.versionName
                return packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return versionName
        }

        /**
         * 获取app版本号
         *
         * @param context 上下文
         * @return app版本号
         */
        fun getAppVersionCode(context: Context): Int {
            try {
                val pm = context.packageManager
                val packageInfo = pm.getPackageInfo(context.packageName, 0)
                return packageInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return -1
        }

        /**
         * 获取manifest里面的 metadata
         */
        fun getMetaValue(context: Context?, metaKey: String?): String? {
            var metaData: Bundle? = null
            var apiKey: String? = null
            if (context == null || metaKey == null) {
                return null
            }
            try {
                val ai = context.packageManager
                    .getApplicationInfo(
                        context.packageName,
                        PackageManager.GET_META_DATA
                    )
                if (null != ai) {
                    metaData = ai.metaData
                }
                if (null != metaData) {
                    apiKey = metaData.getString(metaKey)
                }
            } catch (e: PackageManager.NameNotFoundException) {
            }
            return apiKey
        }

        /**
         * 获取app包名
         *
         * @param context 上下文
         * @return 包名
         */
        fun getPackageName(context: Context): String? {
            return context.packageName
        }

        /**
         * 获取app图标
         *
         * @param context 上下文
         * @return Drawable
         */
        fun geAppIcon(context: Context): Drawable? {
            return getAppIcon(context, getPackageName(context)?: "")
        }

        /**
         * 获取app图标
         *
         * @param context     上下文
         * @param packageName app包名
         * @return Drawable
         */
        fun getAppIcon(context: Context, packageName: String): Drawable? {
            try {
                val pm = context.packageManager
                val info = pm.getApplicationInfo(packageName, 0)
                return info.loadIcon(pm)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 获取app名称
         *
         * @param context     上下文
         * @param packageName app包名
         * @return app名称
         */
        fun getAppName(context: Context, packageName: String): String? {
            try {
                val pm = context.packageManager
                val info = pm.getApplicationInfo(packageName, 0)
                return info.loadLabel(pm).toString()
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 获取app的权限列表
         *
         * @param context     上下文
         * @param packageName app包名
         * @return 权限列表
         */
        fun getAppPermission(context: Context, packageName: String): Array<String?>? {
            try {
                val pm = context.packageManager
                val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                return packageInfo.requestedPermissions
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 获取app的应用签名
         *
         * @param context     上下文
         * @param packageName app包名
         * @return 应用签名
         */
        fun getAppSignature(context: Context, packageName: String): String? {
            try {
                val pm = context.packageManager
                val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                return packageInfo.signatures[0].toCharsString()
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 判断app是否运行在后台
         *
         * @param context 上下文
         * @return boolean
         */
        fun isAppInBackground(context: Context): Boolean {
            val am = context
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val taskList = am.getRunningTasks(1)
            if (taskList != null && !taskList.isEmpty()) {
                val topActivity = taskList[0].topActivity
                return (topActivity != null
                        && topActivity.packageName != context.packageName)
            }
            return false
        }

    }

}