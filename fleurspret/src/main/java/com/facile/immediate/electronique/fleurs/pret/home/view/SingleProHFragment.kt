package com.facile.immediate.electronique.fleurs.pret.home.view

import android.content.Intent
import android.graphics.Rect
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.arthur.commonlib.utils.DensityUtils
import com.facile.immediate.electronique.fleurs.pret.choosegold.view.ChooseGoldActivity
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.ext.addThousandSeparator
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentHomeBinding
import com.facile.immediate.electronique.fleurs.pret.home.vm.FirstViewModel
import com.facile.immediate.electronique.fleurs.pret.input.view.InputInformationActivity
import com.facile.immediate.electronique.fleurs.pret.login.LogUpActivity
import com.facile.immediate.electronique.fleurs.pret.main.FeatureAdapter
import com.facile.immediate.electronique.fleurs.pret.main.UniqueFeatureUtil

class SingleProHFragment : BaseMVVMFragment<FragmentHomeBinding, FirstViewModel>() {
    private val homeVM: FirstViewModel by viewModels(ownerProducer = { requireParentFragment() })
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

        mBinding.tvAskNow.setOnClickListener {
            if (!UserManager.isLogUp()) {
                startActivity(Intent(requireContext(), LogUpActivity::class.java))
                return@setOnClickListener
            }
            mViewModel.verifyIsNetworkAvailable {
//                startActivity(Intent(requireContext(), InputInformationActivity::class.java))
                startActivity(Intent(requireContext(), ChooseGoldActivity::class.java))
            }
        }
    }

    override fun onPageResume() {
        super.onPageResume()
        if (UserManager.isLogUp()) {
            mViewModel.multiProH()
        } else {
            mViewModel.globalSetting("afraidDecemberSlimClassicalTechnology,brownTopic")
        }
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()

        homeVM.refreshCompleteLiveData.observe(viewLifecycleOwner){
            if (UserManager.isLogUp()) {
                mViewModel.multiProH()
            } else {
                mViewModel.globalSetting("afraidDecemberSlimClassicalTechnology,brownTopic")
            }
        }
        mViewModel.singleProHLiveData.observe(viewLifecycleOwner) {
            it?.let { singlePro ->
                mBinding.tvMaxAmount.text =
                    singlePro.afraidDecemberSlimClassicalTechnology.addThousandSeparator()
                singlePro.afraidDecemberSlimClassicalTechnology?.let { amount ->
                    mBinding.drvDividingRuler.setCurMaxMount(amount)
                }
            }
        }
        mViewModel.globalSettingLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.afraidDecemberSlimClassicalTechnology?.isNotEmpty() == true) {
                    mBinding.tvMaxAmount.text =
                        it.afraidDecemberSlimClassicalTechnology.addThousandSeparator()
                    mBinding.drvDividingRuler.setCurMaxMount(it.afraidDecemberSlimClassicalTechnology)
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