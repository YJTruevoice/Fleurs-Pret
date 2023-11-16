package com.facile.immediate.electronique.fleurs.pret.input.view

import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.BottomSheet
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.bean.CommonChooseListItem
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigType
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityInputGatheringInformationBinding
import com.facile.immediate.electronique.fleurs.pret.input.InputUtil
import com.facile.immediate.electronique.fleurs.pret.input.vm.GatheringInputVM
import com.gyf.immersionbar.ImmersionBar

class InputGatheringInformationActivity :
    BaseMVVMActivity<ActivityInputGatheringInformationBinding, GatheringInputVM>() {

    private var gatheringSelectedItem: CommonChooseListItem? = null

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

        mBinding.etAccountNo.addTextChangedListener(mViewModel.textWatcher)
        mBinding.etAccountNoConfirm.addTextChangedListener(mViewModel.textWatcher)

        mBinding.tvGatheringWay.setOnClickListener {
            mViewModel.config(ConfigType.collectionType)
        }

        mBinding.tvNext.setOnClickListener {
            if (!isNextBtnEnable()) {
                Toaster.showToast(AppKit.context.getString(R.string.veuilltext_ez_compl_ter_toutes_les_informations))
                return@setOnClickListener
            }
            if (!differAccountNo()) {
                Toaster.showToast(getString(R.string.text_les_num_ros_de_carte_bancaire_saisis_deux_fois_sont_incoh_rents))
                return@setOnClickListener
            }

            mViewModel.saveGatheringInfo(
                bankCode = "",
                bankName = "",
                bankAccountType = "",
                bankAccountNumber = ""
            )
        }
    }

    private fun initTitleBar() {
        mBinding.inTitleBar.tvTitle.text =
            getString(R.string.text_informations_de_compte_de_r_ception)
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.configLiveData.observe(this) { pair ->
            val list = mutableListOf<CommonChooseListItem>()
            pair?.second?.forEach {
                list.add(CommonChooseListItem(it.value, it.code))
            }
            BottomSheet.showListBottomSheet(this, list, selected = gatheringSelectedItem) {
                when (pair?.first) {
                    ConfigType.collectionType -> {
                        mBinding.tvGatheringWay.text = it.name
                        isNextBtnEnable()
                        gatheringSelectedItem = it
                    }

                    else -> {}
                }
            }
        }
        mViewModel.textWatcherLiveData.observe(this) {
            isNextBtnEnable()
        }
    }

    private fun isNextBtnEnable(): Boolean {
        mBinding.tvNext.isSelected = InputUtil.nextBtnEnable(
            mBinding.tvGatheringWay,
            mBinding.etAccountNo,
            mBinding.etAccountNoConfirm
        )
        return mBinding.tvNext.isSelected
    }

    private fun differAccountNo(): Boolean {
        return mBinding.etAccountNo.text.toString() == mBinding.etAccountNoConfirm.text.toString()
    }
}