package com.facile.immediate.electronique.fleurs.pret.input.view

import android.text.SpannableStringBuilder
import android.view.View
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.BottomSheet
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.bean.CommonChooseListItem
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigType
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.common.ext.onClick
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityInputGatheringInformationBinding
import com.facile.immediate.electronique.fleurs.pret.input.InputUtil
import com.facile.immediate.electronique.fleurs.pret.input.vm.GatheringInputVM
import com.gyf.immersionbar.ImmersionBar

class InputGatheringInformationActivity :
    BaseMVVMActivity<ActivityInputGatheringInformationBinding, GatheringInputVM>() {

    private var collectionTypeSelectedItem: CommonChooseListItem? = null
    private var bankAccountTypeSelectedItem: CommonChooseListItem? = null
    private var bankSelectedItem: CommonChooseListItem? = null

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

    override fun processLogic() {
        super.processLogic()
        mBinding.tvStateTips.visibility =
            if (mViewModel.ordId.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun setListener() {
        super.setListener()
        mBinding.inTitleBar.ivBack.setOnClickListener {
            finish()
        }
        mBinding.inTitleBar.ivCustomer.onClick {
            ConsumerActivity.goBranch(this)
        }

        mBinding.etAccountNo.addTextChangedListener(mViewModel.textWatcher)
        mBinding.etAccountNoConfirm.addTextChangedListener(mViewModel.textWatcher)

        mBinding.tvGatheringWay.onClick {
            mViewModel.config(ConfigType.bankNameList)
        }

        mBinding.tvNext.onClick {
            if (!isNextBtnEnable()) {
                Toaster.showToast(getString(R.string.veuilltext_ez_compl_ter_toutes_les_informations))
                return@onClick
            }
            if (!differAccountNo()) {
                Toaster.showToast(getString(R.string.text_veuillez_remplir_les_m_mes_informations_de_compte))
                return@onClick
            }
//            if (collectionTypeSelectedItem?.value == "1") {
//                mViewModel.config(ConfigType.bankAccountType)
//            } else {
//            }
            mViewModel.saveGatheringInfo(
                bankAccountNumber = mBinding.etAccountNoConfirm.text.toString(),
                bankName = bankSelectedItem?.name.toString(),
                bankCode = bankSelectedItem?.value.toString(),
            )
        }
    }

    private fun initTitleBar() {
        mBinding.inTitleBar.tvTitle.text =
            getString(R.string.text_informations_de_compte_de_r_ception)
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.gatheringLiveData.observe(this) {
            it?.let {
                if (it.isNotEmpty()) {
                    val gathering = it[0]
                    mBinding.etAccountNo.text =
                        SpannableStringBuilder(gathering.lastBuildingTroublesomeRainbowChapter)
                    mBinding.etAccountNoConfirm.text =
                        SpannableStringBuilder(gathering.lastBuildingTroublesomeRainbowChapter)

                    if (gathering.nativeShirtGrocerYesterday?.isNotEmpty() == true
                        && gathering.unablePolePacificShop?.isNotEmpty() == true
                    ) {
                        mBinding.tvGatheringWay.text = gathering.nativeShirtGrocerYesterday
                        bankSelectedItem = CommonChooseListItem(
                            gathering.nativeShirtGrocerYesterday,
                            gathering.unablePolePacificShop
                        )
                    }
                }
                isNextBtnEnable()
            }
        }
        mViewModel.configLiveData.observe(this) { pair ->
            val list = mutableListOf<CommonChooseListItem>()
            pair?.second?.forEach {
                list.add(CommonChooseListItem(it.value, it.code))
            }
            val selectedItem: CommonChooseListItem? = when (pair?.first) {
                ConfigType.collectionType -> {
                    collectionTypeSelectedItem
                }

                ConfigType.bankAccountType -> {
                    bankAccountTypeSelectedItem
                }

                ConfigType.bankNameList -> {
                    bankSelectedItem
                }

                else -> {
                    null
                }
            }
            BottomSheet.showListBottomSheet(this, list, selected = selectedItem) {
                when (pair?.first) {
                    ConfigType.collectionType -> {
                        mBinding.tvGatheringWay.text = it.name
                        if (collectionTypeSelectedItem?.value != it.value) {
                            mBinding.etAccountNo.text = SpannableStringBuilder("")
                            mBinding.etAccountNoConfirm.text = SpannableStringBuilder("")
                        }
                        collectionTypeSelectedItem = it
                        isNextBtnEnable()
                    }

                    ConfigType.bankAccountType -> {
                        bankAccountTypeSelectedItem = it
//                        mViewModel.config(ConfigType.bankNameList)
                    }

                    ConfigType.bankNameList -> {
                        mBinding.tvGatheringWay.text = it.name
                        if (bankSelectedItem?.value != it.value) {
                            mBinding.etAccountNo.text = SpannableStringBuilder("")
                            mBinding.etAccountNoConfirm.text = SpannableStringBuilder("")
                        }
                        bankSelectedItem = it
                        isNextBtnEnable()
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