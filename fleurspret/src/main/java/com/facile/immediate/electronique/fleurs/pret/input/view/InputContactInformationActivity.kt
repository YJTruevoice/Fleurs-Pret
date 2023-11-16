package com.facile.immediate.electronique.fleurs.pret.input.view

import android.view.MotionEvent
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.BottomSheet
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.bean.CommonChooseListItem
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigType
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityInputContactInformationBinding
import com.facile.immediate.electronique.fleurs.pret.input.InputUtil
import com.facile.immediate.electronique.fleurs.pret.input.vm.ContactInputVM
import com.gyf.immersionbar.ImmersionBar

class InputContactInformationActivity :
    BaseMVVMActivity<ActivityInputContactInformationBinding, ContactInputVM>() {

    private var firstShipSelectedItem: CommonChooseListItem? = null
    private var secShipSelectedItem: CommonChooseListItem? = null

    override fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(statusBarDarkMode)
            .fitsSystemWindows(true)
            .init()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
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
            mViewModel.config(ConfigType.relationship)
        }
        mBinding.inContact2st.tvRelation.setOnClickListener {
            mViewModel.config(ConfigType.secRelationship)
        }

        mBinding.inContact1st.etPhone.addTextChangedListener(mViewModel.textWatcher)
        mBinding.inContact2st.etPhone.addTextChangedListener(mViewModel.textWatcher)

        mBinding.tvNext.setOnClickListener {
            if (!isNextBtnEnable()) {
                Toaster.showToast(AppKit.context.getString(R.string.veuilltext_ez_compl_ter_toutes_les_informations))
                return@setOnClickListener
            }
            mViewModel.saveContactInfo(
                phoneNumber = mBinding.inContact1st.etPhone.text.toString(),
                name = mBinding.inContact1st.etNom.text.toString(),
                relationship = "",
                phoneNumberSec = mBinding.inContact2st.etPhone.text.toString(),
                nameSec = mBinding.inContact2st.etNom.text.toString(),
                relationshipSec = ""
            )
        }
    }

    private fun initTitleBar() {
        mBinding.inTitleBar.tvTitle.text = getString(R.string.text_contacts_en_cas_d_urgence)
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.configLiveData.observe(this) { pair ->
            val list = mutableListOf<CommonChooseListItem>()
            pair?.second?.forEach {
                list.add(CommonChooseListItem(it.value, it.code))
            }

            when (pair?.first) {
                ConfigType.relationship -> {
                    BottomSheet.showListBottomSheet(this, list, selected = firstShipSelectedItem) {
                        mBinding.inContact1st.tvRelation.text = it.name
                        isNextBtnEnable()
                        firstShipSelectedItem = it
                    }
                }

                ConfigType.secRelationship -> {
                    BottomSheet.showListBottomSheet(this, list, selected = secShipSelectedItem) {
                        mBinding.inContact2st.tvRelation.text = it.name
                        isNextBtnEnable()
                        secShipSelectedItem = it
                    }
                }

                else -> {}
            }
        }

        mViewModel.textWatcherLiveData.observe(this) {
            isNextBtnEnable()
        }
    }

    private fun isNextBtnEnable(): Boolean {
        mBinding.tvNext.isSelected = InputUtil.nextBtnEnable(
//            mBinding.inContact1st.tvRelation,
            mBinding.inContact1st.etPhone,
            mBinding.inContact1st.etNom,
//            mBinding.inContact2st.tvRelation,
            mBinding.inContact2st.etPhone,
            mBinding.inContact2st.etNom
        )
        return mBinding.tvNext.isSelected
    }
}