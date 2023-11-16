package com.facile.immediate.electronique.fleurs.pret.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.AppConstants
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityWebBinding

class WebActivity : BaseBindingActivity<ActivityWebBinding>() {
    private var mUrl: String? = null
    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        mBinding.webView.apply {
            this.settings.apply {
                domStorageEnabled = true
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = true
                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)
                allowFileAccess = true
                loadsImagesAutomatically = true
            }
            // 加快网页加载速度
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }

    }

    override fun buildView() {
        super.buildView()
        mBinding.inTitleBar.tvTitle.visibility = View.GONE
        mBinding.inTitleBar.ivCustomer.setImageResource(R.drawable.ic_close_black_28dp)
    }

    override fun setListener() {
        super.setListener()
        mBinding.inTitleBar.ivCustomer.setOnClickListener {
            finish()
        }
        mBinding.inTitleBar.ivBack.setOnClickListener {
            goBack()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun goBack() {
        if (mBinding.webView.canGoBack()) {
            mBinding.webView.goBack()
        } else {
            finish()
        }
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