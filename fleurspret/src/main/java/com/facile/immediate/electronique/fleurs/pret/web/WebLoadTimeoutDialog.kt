package com.facile.immediate.electronique.fleurs.pret.web

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.DialogWebLoadTimeoutLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.BaseDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseDialog

class WebLoadTimeoutDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.CustomDialog
) : BaseDialog(context, themeResId) {

    private lateinit var mBinding: DialogWebLoadTimeoutLayoutBinding


    override fun getContentView(): View {
        mBinding = DialogWebLoadTimeoutLayoutBinding.inflate(LayoutInflater.from(context))
        return mBinding.root
    }

    override fun setDialogData(config: BaseDialogConfigEntity?) {
        (config)?.let {
            config.touchOutsideCancelAble = false
            // 公共初始化
            super.setDialogData(config)

            mBinding.ivClose.apply {
                setOnClickListener {
                    dismiss()
                }
            }

            mBinding.tvUpdate.apply {
                setOnClickListener {
                    config.confirmCallback?.invoke(this@WebLoadTimeoutDialog)
                    dismiss()
                }
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
    }

    companion object {
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    class Builder constructor(var context: Context) : BaseDialog.Builder<Builder>(context) {
        override fun createDialog(): BaseDialog {
            return WebLoadTimeoutDialog(context)
        }

        override fun createConfig(): BaseDialogConfigEntity {
            return BaseDialogConfigEntity()
        }

    }

}