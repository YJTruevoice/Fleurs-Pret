package com.facile.immediate.electronique.fleurs.pret.app

import android.app.Application
import android.content.res.Configuration
import com.arthur.baselib.utils.AppLanguageUtil
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.ActivityManager
import com.facebook.stetho.Stetho
import com.facile.immediate.electronique.fleurs.pret.BuildConfig
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import com.facile.immediate.electronique.fleurs.pret.view.toast.TextToastImpl

/**
 * @Author arthur
 * @Date 2023/10/17
 * @Description
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppLanguageUtil.setAppLanguage(this)
        registerActivityLifecycleCallbacks(ActivityManager)
        AppKit.initContext(this.applicationContext)
        Toaster.init(this.applicationContext).register(toast = TextToastImpl())
        NetMgr.get().init(this.applicationContext)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AppLanguageUtil.setAppLanguage(this)
    }
}