package com.facile.immediate.electronique.fleurs.pret.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.AppConstants
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityWebBinding
import com.gyf.immersionbar.ImmersionBar

class WebActivity : BaseBindingActivity<ActivityWebBinding>() {
    private var mUrl: String? = null
    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        mBinding.webView.apply {
            this.settings.apply {
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                domStorageEnabled = true
                allowFileAccess = true
                loadsImagesAutomatically = true
                //专有配置
                settings.setSupportMultipleWindows(true)
            }
            // 加快网页加载速度
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
        }

    }

    override fun setStatusBar() {
        ImmersionBar.with(this).fullScreen(true).init()
    }

    override fun onResume() {
        super.onResume()
        mBinding.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.webView.onPause()
    }

    override fun onDestroy() {
        mBinding.webView.apply {
            this.removeAllViews()
            destroy()
        }
        super.onDestroy()
    }

    override fun processLogic() {
        super.processLogic()
        mUrl = intent.getStringExtra(AppConstants.KEY_WEB_VIEW_URL)
        mUrl?.let { mBinding.webView.loadUrl(it) } ?: let { Toaster.showToast("") }
    }

    companion object {
        fun open(context: Context, url: String) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(AppConstants.KEY_WEB_VIEW_URL, url)
            })
        }
    }
}