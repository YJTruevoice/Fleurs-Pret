package com.arthur.network.dialog

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.arthur.commonlib.ability.ILoading
import com.arthur.network.R
import com.arthur.network.databinding.LayoutLoadingDialogDefaultBinding

class DefaultLoadingDialog(context: Context, themeResId: Int = R.style.Theme_NetLoadingDialog) :
    Dialog(context, themeResId) {

    /**
     * 转圈圈动画
     */
    private var mRotationAnimator: ObjectAnimator? = null

    private var mBinding: LayoutLoadingDialogDefaultBinding

    init {
        window?.setBackgroundDrawableResource(R.color.common_translate_bg)
        mBinding = LayoutLoadingDialogDefaultBinding.inflate(LayoutInflater.from(context))
        setContentView(mBinding.root)

        mRotationAnimator = ObjectAnimator.ofFloat(
            mBinding.ivLoadingDialog,
            "rotation",
            0f,
            360f
        ).setDuration(1000).apply {
            interpolator = LinearInterpolator()
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            start()
        }

        setOnDismissListener {
            stopAnim()
        }

        // 防止泄漏
        (context as? AppCompatActivity)?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                if (isShowing) {
                    dismiss()
                }
            }
        })
    }

    private fun stopAnim() {
        mRotationAnimator?.cancel()
        mRotationAnimator = null
    }

    private fun setConfig(config: Builder.NCLoadingDialogConfig): DefaultLoadingDialog {

        mBinding.tvLoadingDialog.apply {
            if (config.message.isNotEmpty()) {
                text = config.message
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
        setCancelable(config.isCancelable)
        return this
    }

    companion object {
        fun with(ac: Activity): Builder {
            return Builder(ac)
        }
    }

    class Builder constructor(private val context: Context) {

        private val dialog by lazy {
            DefaultLoadingDialog(context)
        }

        private val config = NCLoadingDialogConfig()

        fun cancelable(cancelable: Boolean): Builder {
            config.isCancelable = cancelable
            return this
        }

        fun message(message: String): Builder {
            config.message = message
            return this
        }

        fun build(): Dialog {
            return dialog.setConfig(config)
        }

        fun show() {
            build().show()
        }

        data class NCLoadingDialogConfig(
            /**
             * 是否可以由用户主动关闭
             */
            var isCancelable: Boolean = false,
            /**
             * 加载文案
             */
            var message: String = "加载中..."
        )

    }

    object NetLoading : ILoading {

        private var loadingDialog: Dialog? = null

        override fun startLoading(ac: Activity) {
            startLoading(ac, "")
        }

        override fun startLoading(ac: Activity, message: String) {
            startLoading(ac, message, false)
        }

        override fun closeLoading() {
            try {
                if (loadingDialog != null && loadingDialog?.isShowing == true
                    && loadingDialog?.ownerActivity != null && loadingDialog?.ownerActivity?.isFinishing == false
                    && loadingDialog?.ownerActivity?.isDestroyed == false
                ) {
                    loadingDialog?.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                loadingDialog = null
            }
        }

        fun startLoading(ac: Activity, message: String, cancelable: Boolean = false) {
            if (ac.isFinishing) {
                return
            }

            if (loadingDialog?.isShowing == true) {
                return
            } else {
                loadingDialog = createLoadingDialog(ac, message, cancelable)
            }
            loadingDialog?.show()
        }

        private fun createLoadingDialog(ac: Activity, message: String, cancelable: Boolean = false): Dialog {
            val loadingDialog = with(ac)
                .message(message)
                .cancelable(cancelable)
                .build().apply {
                    setCanceledOnTouchOutside(false)
                }
            loadingDialog.setOwnerActivity(ac)
            return loadingDialog
        }
    }

}