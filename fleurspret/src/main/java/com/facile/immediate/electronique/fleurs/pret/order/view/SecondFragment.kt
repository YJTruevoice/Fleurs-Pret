package com.facile.immediate.electronique.fleurs.pret.order.view

import android.view.View
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentDashboardBinding
import com.facile.immediate.electronique.fleurs.pret.order.vm.SecondViewModel

class SecondFragment : BaseMVVMFragment<FragmentDashboardBinding, SecondViewModel>() {
    private val ordArrAdapter: OrdArrAdapter by lazy {
        OrdArrAdapter()
    }

    override fun buildView() {
        super.buildView()
        mBinding.tvTitleBarTitle.apply {
            layoutParams.height = 44f.dp2px(requireContext()) + 32f.dp2px(requireContext())
            updatePadding(top = 32f.dp2px(requireContext()))
        }
        mBinding.rvOrd.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ordArrAdapter
        }
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.ordArrLiveEvent.observe(this) {
            if (it.isNullOrEmpty()) {
                mBinding.llEmpty.visibility = View.VISIBLE
                mBinding.rvOrd.visibility = View.GONE
            } else {
                mBinding.llEmpty.visibility = View.GONE
                mBinding.rvOrd.visibility = View.VISIBLE
                ordArrAdapter.setData(it)
            }
        }
    }
}