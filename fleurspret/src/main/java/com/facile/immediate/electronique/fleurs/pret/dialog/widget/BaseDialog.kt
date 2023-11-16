package com.facile.immediate.electronique.fleurs.pret.dialog.widget

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import com.arthur.commonlib.utils.DensityUtils
import com.arthur.commonlib.utils.ScreenUtils
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.BaseDialogConfigEntity

abstract class BaseDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.CustomDialog
) : Dialog(context, themeResId) {

    var config: BaseDialogConfigEntity? = null

    init {
        setContentView(getContentView())
        initView()
        setListener()
    }

    /**
     * 设置参数
     */
    open fun setDialogData(config: BaseDialogConfigEntity?) {
        if (config == null) {
            return
        }

        this.config = config
        invalidate()
    }

    /**
     * 重新绑定值
     */
    private fun invalidate() {
        config?.let { config ->

            // 是否可关闭
            setCancelable(config.backCancelAble)
            setCanceledOnTouchOutside(config.touchOutsideCancelAble)
        }
    }

    /**
     * 设置监听
     */
    protected open fun setListener() {
    }

    override fun show() {
        WindowManager.LayoutParams().apply {
            copyFrom(window?.attributes)
            width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 15f) * 2
            super.show()
            window?.attributes = this
        }
    }

    private fun initView() {
    }

    abstract fun getContentView(): View

    abstract class Builder<B : Builder<B>> constructor(context: Context) {
        private val dialog by lazy {
            createDialog()
        }
        protected val config by lazy {
            createConfig()
        }

        protected abstract fun createDialog(): BaseDialog

        protected abstract fun createConfig(): BaseDialogConfigEntity

        /**
         * 标题
         */
        open fun title(title: String?): B {
            config.title = title
            return this as B
        }

        /**
         * 取消动作
         */
        open fun cancel(cancelText: String?, cb: ((BaseDialog) -> Unit)? = null): B {
            config.cancelText = cancelText
            config.cancelCallback = cb
            return this as B
        }

        /**
         * 确认动作
         */
        open fun confirm(confirmText: String?, cb: ((BaseDialog) -> Unit)?): B {
            config.confirmText = confirmText
            config.confirmCallback = cb
            return this as B
        }

        /**
         * 标题图标资源ID - 和url同时设置优先级低于url
         */
        open fun titleIc(resId: Int): B {
            if (resId > 0) {
                config.titleIcResId = resId
            }
            return this as B
        }

        /**
         * 标题图标链接 - 和resId同时设置优先级高于resId
         */
        open fun titleIc(url: String?): B {
            url?.let {
                config.titleIcUrl = it
            }
            return this as B
        }

        /**
         * 展示关闭按钮
         */
        open fun showClose(showClose: Boolean): B {
            config.showClose = showClose
            return this as B
        }

        /**
         * 是否可返回关闭
         */
        open fun backCancelAble(backCancelAble: Boolean): B {
            config.backCancelAble = backCancelAble
            return this as B
        }

        /**
         * 点击外部是否消失
         */
        open fun touchOutsideCancelAble(touchOutsideCancelAble: Boolean): B {
            config.touchOutsideCancelAble = touchOutsideCancelAble
            return this as B
        }

        /**
         * 点击按钮时是否默认关闭
         */
        open fun dismissOnBtnClick(dismissOnBtnClick: Boolean): B {
            config.dismissOnBtnClick = dismissOnBtnClick
            return this as B
        }

        fun build(): BaseDialog {
            dialog.setDialogData(config)
            return dialog
        }

        open fun show(): BaseDialog {
            return build().apply {
                show()
            }
        }
    }
}