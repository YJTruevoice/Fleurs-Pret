package com.arthur.baselib.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.Locale

/**
 * 当然这里我的思路是,本地缓存语言列表索引,然后后续根据 id 直接获取对应的语言即可.

点击确认时,进行缓存当前选择的

override fun onClick(v: View?) {
when (v?.id) {
R.id.tvDone -> {
// 更新选择状态
LocalDataStorage().multilingual = mAfterPosition
setAppLanguage(this)
reStartActivity()
}
}
}

private fun reStartActivity() {
val intent = Intent(mSelfActivity, MainActivity::class.java)
intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
startActivity(intent)
// 取消其专场动画
overridePendingTransition(0, 0)
}
复制
五、Application 中 Configuration 处理

override fun onConfigurationChanged(newConfig: Configuration?) {
super.onConfigurationChanged(newConfig)
// ...
setAppLanguage(this)
}
复制
六、BaseActivity 处理

由于需要重建 Activity 去处理对应资源,所以这里个人是把它放在 BaseActivity 中去处理:

override fun attachBaseContext(newBase: Context?) {
super.attachBaseContext(newBase?.let { getAttachBaseContext(it) })
}
 */
object AppLanguageUtil {

    /**
     * Activity 更新语言资源
     */
    fun getAttachBaseContext(context: Context): Context {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return setAppLanguageApi24(context)
        } else {
            setAppLanguage(context)
        }
        return context
    }

    /**
     * 设置应用语言
     */
    @Suppress("DEPRECATION")
    fun setAppLanguage(context: Context) {
        val resources = context.resources
        val displayMetrics = resources.displayMetrics
        val configuration = resources.configuration

        val locale = getAppLocale()
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, displayMetrics)
    }

    /**
     * compatible 7.0 or upper
     */
    @TargetApi(Build.VERSION_CODES.N)
    private fun setAppLanguageApi24(context: Context): Context {
        val locale = getAppLocale()
        val resources = context.resources
        val configuration = resources.configuration
        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)
        return context.createConfigurationContext(configuration)
    }

    /**
     * get app current language
     */
    public fun getAppLocale() = when (2) {
        0 -> { // follow system
            getSystemLocale()
        }
        1 -> { // china
            Locale.CHINA
        }
        else -> Locale.FRANCE
    }

    /**
     * get system language
     */
    private fun getSystemLocale(): Locale {
        val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault()[0]
        } else {
            Locale.getDefault()
        }
        return when (systemLocale.language) {
            Locale.CHINA.language -> {
                Locale.CHINA
            }
            else -> {
                Locale.FRANCE
            }
        }
    }
}