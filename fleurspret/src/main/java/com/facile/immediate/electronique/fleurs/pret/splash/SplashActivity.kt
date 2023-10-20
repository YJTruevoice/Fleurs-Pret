package com.facile.immediate.electronique.fleurs.pret.splash

import android.content.Context
import android.content.Intent
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivitySplashBinding
import com.facile.immediate.electronique.fleurs.pret.main.MainActivity
import com.facile.immediate.electronique.fleurs.pret.policy.PolicyActivity
import com.facile.immediate.electronique.fleurs.pret.utils.AppLanguageUtil
import com.facile.immediate.electronique.fleurs.pret.utils.PolicyUtil
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseBindingActivity<ActivitySplashBinding>() {

    override fun setStatusBar() {
        ImmersionBar.with(this).fullScreen(true).init()
    }

    override fun attachBaseLanguageContext(context: Context?): Context? {
        return super.attachBaseLanguageContext(context?.let {
            AppLanguageUtil.getAttachBaseContext(it)
        })
    }

    override fun processLogic() {
        super.processLogic()
        MainScope().launch {
            delay(3000)
            if (PolicyUtil.isPolicyAccepted()) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, PolicyActivity::class.java))
            }
            finish()
        }

    }
}