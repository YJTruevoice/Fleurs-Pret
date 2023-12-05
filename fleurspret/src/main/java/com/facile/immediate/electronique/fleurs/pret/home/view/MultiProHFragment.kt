package com.facile.immediate.electronique.fleurs.pret.home.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.arthur.commonlib.utils.DensityUtils
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentMultiProHomeBinding
import com.facile.immediate.electronique.fleurs.pret.home.view.adapter.MultiProAdapter
import com.facile.immediate.electronique.fleurs.pret.home.vm.FirstViewModel

class MultiProHFragment : BaseMVVMFragment<FragmentMultiProHomeBinding, FirstViewModel>() {

    override fun buildView() {
        super.buildView()
        mBinding.srlRefresh.setEnableRefresh(false)
    }
    override fun setListener() {
        super.setListener()
        mBinding.inTitle.ivCustomer.setOnClickListener {
            ConsumerActivity.go(requireActivity())
        }
    }

    override fun onPageResume() {
        super.onPageResume()
        mViewModel.multiProH()
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.refreshCompleteLiveData.observe(viewLifecycleOwner) {
            if (mBinding.srlRefresh.isRefreshing)
                mBinding.srlRefresh.finishRefresh()
        }

        mViewModel.multiProHLiveData.observe(viewLifecycleOwner) {
            it?.let {
                mBinding.rvMultiProList.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = MultiProAdapter().apply { this.addAll(it) }
                    if (itemDecorationCount < 0) {
                        addItemDecoration(object : ItemDecoration() {
                            override fun getItemOffsets(
                                outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State
                            ) {
                                outRect.set(0, DensityUtils.dp2px(context, 12f), 0, 0)
                            }
                        })
                    }
                }
            }
        }
    }
}