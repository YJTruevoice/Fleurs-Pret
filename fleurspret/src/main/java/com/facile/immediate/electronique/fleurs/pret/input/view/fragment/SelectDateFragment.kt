package com.facile.immediate.electronique.fleurs.pret.input.view.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.arthur.baselib.structure.base.view.BaseBindingFragment
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.utils.DensityUtils
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.IDialogContent
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.BottomSheetDialog
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentSelectDateBinding

class SelectDateFragment(private var selectConfirmed: ((Int, Int, Int) -> Unit)? = null) :
    BaseBindingFragment<FragmentSelectDateBinding>(), IDialogContent {
    override val current: Fragment = this


    override fun buildView() {
        super.buildView()
        arguments?.getString("dateSource")?.let { mBinding.cvCalendar.setDateSource(it) }
    }

    override fun setListener() {
        super.setListener()
        mBinding.tvCalendarConfirm.setOnClickListener {
            dismiss()
            selectConfirmed?.invoke(
                mBinding.cvCalendar.getSelectedYear(),
                mBinding.cvCalendar.getSelectedMonth(),
                mBinding.cvCalendar.getSelectedDayOfMonth()
            )
        }
    }

    companion object {
        fun show(
            ac: FragmentActivity,
            dateSource: String = "",
            onShowListener: DialogInterface.OnShowListener? = null,
            onDismissListener: DialogInterface.OnDismissListener? = null,
            selectConfirmed: ((Int, Int, Int) -> Unit)? = null
        ) {
            show(ac.supportFragmentManager, dateSource,onShowListener, onDismissListener, selectConfirmed)
        }

        fun show(
            fm: FragmentManager,
            dateSource: String = "",
            onShowListener: DialogInterface.OnShowListener? = null,
            onDismissListener: DialogInterface.OnDismissListener? = null,
            selectConfirmed: ((Int, Int, Int) -> Unit)? = null
        ) {
            BottomSheetDialog.withFixedHeight()
                .height(DensityUtils.dp2px(AppKit.context, 500f))
                .wrapHeight(true)
                .content(SelectDateFragment(selectConfirmed).apply {
                    arguments = Bundle().apply { putString("dateSource", dateSource) }
                })
                .cancelOnTouchOutsize(false)
                .onShowListener(onShowListener)
                .onDismissListener(onDismissListener)
                .build()
                .show(fm, "SelectDatePanel")
        }
    }
}