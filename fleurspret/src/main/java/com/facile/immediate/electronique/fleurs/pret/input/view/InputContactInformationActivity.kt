package com.facile.immediate.electronique.fleurs.pret.input.view

import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigType
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityInputContactInformationBinding
import com.facile.immediate.electronique.fleurs.pret.input.vm.ContactVM
import com.gyf.immersionbar.ImmersionBar

class InputContactInformationActivity :
    BaseMVVMActivity<ActivityInputContactInformationBinding, ContactVM>() {
    override fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(statusBarDarkMode)
            .fitsSystemWindows(true)
            .init()
    }

    override fun buildView() {
        super.buildView()
        initTitleBar()

        PrivacyPolicyDisplayUtil.displayPrivacyPolicyGuide(this, mBinding.tvReadPrivacyPolicyGuide)
    }

    override fun setListener() {
        super.setListener()
        mBinding.inTitleBar.ivBack.setOnClickListener {
            finish()
        }
        mBinding.inTitleBar.ivCustomer.setOnClickListener {

        }
        mBinding.inContact1st.tvRelation.setOnClickListener {
            mViewModel.config(ConfigType.constantNovelistMessSureLibrary)
        }
        mBinding.inContact2st.tvRelation.setOnClickListener {
            mViewModel.config(ConfigType.thirstyConditionSaturdayPunctualMiniskirt)
        }
    }

    private fun initTitleBar() {
        mBinding.inTitleBar.tvTitle.text = getString(R.string.text_contacts_en_cas_d_urgence)
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()

    }
}