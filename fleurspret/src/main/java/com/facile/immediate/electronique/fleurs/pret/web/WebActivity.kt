package com.facile.immediate.electronique.fleurs.pret.web

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.facile.immediate.electronique.fleurs.pret.AppConstants
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityWebBinding

class WebActivity : BaseBindingActivity<ActivityWebBinding>() {
    private var mUrl: String = ""

    private var secondsRemaining = 0L
    private val timeoutCountDown = object : CountDownTimer(30000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            secondsRemaining = millisUntilFinished / 1000
        }

        override fun onFinish() {
            WebLoadTimeoutDialog.with(this@WebActivity)
                .confirm(getString(R.string.text_actualizar)) {
                    mBinding.webView.loadUrl(mUrl)
                }
                .build().show()
        }
    }

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

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    timeoutCountDown.start()
                    mBinding.pbProgress.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    timeoutCountDown.cancel()
                    mBinding.pbProgress.visibility = View.GONE
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    mBinding.pbProgress.progress = newProgress
                }
            }
        }

    }

    override fun buildView() {
        super.buildView()
        mBinding.inTitleBar.tvTitle.visibility = View.GONE
        mBinding.inTitleBar.ivCustomer.apply {
            setImageResource(R.mipmap.icon_close_page)
            imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_242237))
        }
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
        mUrl = intent.getStringExtra(AppConstants.KEY_WEB_VIEW_URL) ?: ""
        mBinding.webView.loadUrl(mUrl)
    }

    companion object {
        fun open(context: Context, url: String) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(AppConstants.KEY_WEB_VIEW_URL, url)
            })
        }
    }
}