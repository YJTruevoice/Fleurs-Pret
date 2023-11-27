package com.facile.immediate.electronique.fleurs.pret.order.view

import android.view.View
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentDashboardBinding
import com.facile.immediate.electronique.fleurs.pret.order.vm.SecondViewModel

class SecondFragment : BaseMVVMFragment<FragmentDashboardBinding, SecondViewModel>() {
    private val ordArrAdapter: OrdArrAdapter by lazy {
        OrdArrAdapter()
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
                ordArrAdapter.addAll(it)
            }
        }
    }
}