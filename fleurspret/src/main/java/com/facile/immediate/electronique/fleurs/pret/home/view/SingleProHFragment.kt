package com.facile.immediate.electronique.fleurs.pret.home.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.arthur.commonlib.utils.DensityUtils
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentHomeBinding
import com.facile.immediate.electronique.fleurs.pret.home.vm.FirstViewModel
import com.facile.immediate.electronique.fleurs.pret.main.FeatureAdapter
import com.facile.immediate.electronique.fleurs.pret.main.UniqueFeatureUtil

class SingleProHFragment : BaseMVVMFragment<FragmentHomeBinding, FirstViewModel>() {

    override fun buildView() {
        super.buildView()
        initFeature()
    }

    override fun processLogic() {
        super.processLogic()
        context?.let {
            PrivacyPolicyDisplayUtil.displayPrivacyPolicyGuide(
                it,
                mBinding.tvReadPrivacyPolicyGuide
            )
        }
    }

    override fun setListener() {
        super.setListener()
        mBinding.srlRefresh.setOnRefreshListener {
            mViewModel.singleProH()
        }
    }

    override fun onPageResume() {
        super.onPageResume()
        mBinding.srlRefresh.autoRefresh()
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.refreshCompleteLiveData.observe(viewLifecycleOwner) {
            if (mBinding.srlRefresh.isRefreshing)
                mBinding.srlRefresh.finishRefresh()
        }

        mViewModel.singleProHLiveData.observe(viewLifecycleOwner) {
            it?.let { globalInfo ->
                mBinding.tvMaxAmount.text = globalInfo.afraidDecemberSlimClassicalTechnology
                globalInfo.afraidDecemberSlimClassicalTechnology?.let { amount ->
                    mBinding.drvDividingRuler.setCurMaxMount(amount)
                }
            }
        }
    }

    private fun initFeature() {
        mBinding.rvChooseReason.apply {
            layoutManager = LinearLayoutManager(
                this@SingleProHFragment.requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = FeatureAdapter().apply {
                addAll(UniqueFeatureUtil.generateFeatures())
            }
            if (itemDecorationCount <= 0) {
                addItemDecoration(object : ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        if (parent.indexOfChild(view) > 0) {
                            outRect.set(DensityUtils.dp2px(context, 16f), 0, 0, 0)
                        }
                    }
                })
            }
        }
    }
}