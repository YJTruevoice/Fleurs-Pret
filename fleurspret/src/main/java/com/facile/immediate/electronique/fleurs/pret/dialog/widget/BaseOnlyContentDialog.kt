package com.facile.immediate.electronique.fleurs.pret.dialog.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.DialogOnlyContentLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.BaseDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.CommonDialogConfigEntity

abstract class BaseOnlyContentDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.CustomDialog
) : BaseDialog(context, themeResId) {

    private lateinit var mBinding: DialogOnlyContentLayoutBinding


    override fun getContentView(): View {
        mBinding = DialogOnlyContentLayoutBinding.inflate(LayoutInflater.from(context))
        return mBinding.root
    }

    override fun setDialogData(config: BaseDialogConfigEntity?) {
        (config as? CommonDialogConfigEntity)?.let {
            config.touchOutsideCancelAble = true
            // 公共初始化
            super.setDialogData(config)

            // 内容
            mBinding.flContent.apply {
                    removeAllViews()
                    addView(contentView())
            }
        }

    }

    abstract fun contentView(): View

}