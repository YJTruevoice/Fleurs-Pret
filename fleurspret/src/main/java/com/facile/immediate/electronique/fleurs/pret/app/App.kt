package com.facile.immediate.electronique.fleurs.pret.app

import android.content.res.Configuration
import androidx.multidex.MultiDexApplication
import com.arthur.baselib.utils.AppLanguageUtil
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Loading
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.ActivityManager
import com.arthur.network.dialog.NetLoading
import com.facebook.stetho.Stetho
import com.facile.immediate.electronique.fleurs.pret.BuildConfig
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import com.facile.immediate.electronique.fleurs.pret.view.toast.TextToastImpl
import im.crisp.client.Crisp

/**
 * @Author arthur
 * @Date 2023/10/17
 * @Description
 */
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        AppLanguageUtil.setAppLanguage(this)
        registerActivityLifecycleCallbacks(ActivityManager)
        AppKit.initContext(this.applicationContext)
        Toaster.init(this.applicationContext).register(toast = TextToastImpl())
        NetMgr.get().init(this.applicationContext)
        Loading.init(NetLoading)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        // Replace it with your WEBSITE_ID
        // Retrieve it using https://app.crisp.chat/website/[YOUR_WEBSITE_ID]/
        Crisp.configure(this.applicationContext, "08e6a12b-c522-4bc8-857f-3bdaaa96c0ad")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AppLanguageUtil.setAppLanguage(this)
    }
}