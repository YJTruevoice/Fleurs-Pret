package com.facile.immediate.electronique.fleurs.pret.app

import android.app.Application
import android.content.res.Configuration
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.ActivityManager
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import com.arthur.baselib.utils.AppLanguageUtil

/**
 * @Author arthur
 * @Date 2023/10/17
 * @Description
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityManager)
        AppKit.initContext(this.applicationContext)
        Toaster.init(this.applicationContext)
        NetMgr.get().init(this.applicationContext)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AppLanguageUtil.setAppLanguage(this)
    }
}