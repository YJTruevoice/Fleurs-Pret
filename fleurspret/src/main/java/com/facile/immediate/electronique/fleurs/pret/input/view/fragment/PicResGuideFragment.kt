package com.facile.immediate.electronique.fleurs.pret.input.view.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.arthur.baselib.structure.base.view.BaseBindingFragment
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.utils.DensityUtils
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.BottomSheetDialog
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.IDialogContent
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentPicResGuideBinding

class PicResGuideFragment(private var selectConfirmed: ((Int) -> Unit)? = null) :
    BaseBindingFragment<FragmentPicResGuideBinding>(), IDialogContent {
    override val current: Fragment = this

    override fun setListener() {
        super.setListener()
        mBinding.tvTake.setOnClickListener {
            mBinding.tvTake.isSelected = true
            dismiss()
            selectConfirmed?.invoke(1)
        }
        mBinding.tvSelect.setOnClickListener {
            mBinding.tvSelect.isSelected = true
            dismiss()
            selectConfirmed?.invoke(2)
        }
    }

    companion object {
        fun show(
            ac: FragmentActivity,
            selectConfirmed: ((Int) -> Unit)? = null
        ) {
            show(ac.supportFragmentManager, selectConfirmed)
        }

        fun show(
            fm: FragmentManager,
            selectConfirmed: ((Int) -> Unit)? = null
        ) {
            BottomSheetDialog.withFixedHeight()
                .height(DensityUtils.dp2px(AppKit.context, 500f))
                .wrapHeight(true)
                .content(PicResGuideFragment(selectConfirmed))
                .cancelOnTouchOutsize(true)
                .build()
                .show(fm, "PicResGuidePanel")
        }
    }
}