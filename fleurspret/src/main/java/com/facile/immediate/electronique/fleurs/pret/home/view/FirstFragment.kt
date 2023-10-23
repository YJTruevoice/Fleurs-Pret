package com.facile.immediate.electronique.fleurs.pret.home.view

import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentHomeBinding
import com.facile.immediate.electronique.fleurs.pret.home.vm.FirstViewModel
import com.facile.immediate.electronique.fleurs.pret.web.WebActivity

class FirstFragment : BaseMVVMFragment<FragmentHomeBinding, FirstViewModel>() {

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
        mBinding.ivBannerLink.setOnClickListener {
            context?.let {
                mViewModel.globalInfo?.brownTopic?.let { url -> WebActivity.open(it, url) }
            }
        }
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()

        mViewModel.globalInfoLiveData.observe(viewLifecycleOwner) {
            it?.let { globalInfo ->
                mBinding.tvMaxAmount.text = globalInfo.afraidDecemberSlimClassicalTechnology
            }
        }
    }
}