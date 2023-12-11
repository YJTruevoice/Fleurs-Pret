package com.facile.immediate.electronique.fleurs.pret.input.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.view.MotionEvent
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.BottomSheet
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.bean.CommonChooseListItem
import com.facile.immediate.electronique.fleurs.pret.common.EditTextFilter
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigType
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.common.ext.onClick
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityInputContactInformationBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseConfirmCancelDialog
import com.facile.immediate.electronique.fleurs.pret.input.InputUtil
import com.facile.immediate.electronique.fleurs.pret.input.vm.ContactInputVM
import com.gyf.immersionbar.ImmersionBar
import com.permissionx.guolindev.PermissionX


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

        mBinding.apply {
            inContact1st.etPhone.filters =
                arrayOf(EditTextFilter.getPhoneEditFilter(), EditTextFilter.getEditLengthFilter(20))
            inContact2st.etPhone.filters =
                arrayOf(EditTextFilter.getPhoneEditFilter(), EditTextFilter.getEditLengthFilter(20))
        }
    }

    override fun setListener() {
        super.setListener()
        mBinding.inTitleBar.ivBack.setOnClickListener {
            finish()
        }
        mBinding.inTitleBar.ivCustomer.onClick {
            ConsumerActivity.goBranch(this)
        }
        mBinding.inContact1st.tvRelation.onClick {
            mViewModel.config(ConfigType.relationship)
        }
        mBinding.inContact2st.tvRelation.onClick {
            mViewModel.config(ConfigType.secRelationship)
        }

        mBinding.inContact1st.etPhone.addTextChangedListener(mViewModel.textWatcher)
        mBinding.inContact1st.etNom.addTextChangedListener(mViewModel.textWatcher)
        mBinding.inContact1st.etPhone.addTextChangedListener(mViewModel.textWatcher)
        mBinding.inContact2st.etNom.addTextChangedListener(mViewModel.textWatcher)

        mBinding.tvNext.onClick {
            if (!isNextBtnEnable()) {
                Toaster.showToast(getString(R.string.veuilltext_ez_compl_ter_toutes_les_informations))
                return@onClick
            }
            val fistNumber = mBinding.inContact1st.etPhone.text.toString()
            val secNumber = mBinding.inContact2st.etPhone.text.toString()
            if (fistNumber.length < 9 || secNumber.length < 9) {
                Toaster.showToast(getString(R.string.text_veuillez_d_abord_remplir_le_bon_num_ro_de_t_l_phone_portable))
                return@onClick
            }
            mViewModel.saveContactInfo(
                phoneNumber = mBinding.inContact1st.etPhone.text.toString(),
                name = mBinding.inContact1st.etNom.text.toString(),
                relationship = firstShipSelectedItem?.value?.toString() ?: "",
                phoneNumberSec = mBinding.inContact2st.etPhone.text.toString(),
                nameSec = mBinding.inContact2st.etNom.text.toString(),
                relationshipSec = secShipSelectedItem?.value?.toString() ?: ""
            )
        }
    }

    private fun initTitleBar() {
        mBinding.inTitleBar.tvTitle.text = getString(R.string.text_contacts_en_cas_d_urgence)
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.userBasicLiveData.observe(this) {
            it?.let {
                mBinding.apply {
                    inContact1st.apply {
                        tvRelation.text = it.secondViolentTightPilot
                        etPhone.text =
                            SpannableStringBuilder(it.usualExtraordinaryScholarshipQuickHardship)
                        etNom.text =
                            SpannableStringBuilder(it.irishGradeUndergroundAmericanPostcard)
                    }
                    if (it.secondViolentTightPilot?.isNotEmpty() == true && it.constantNovelistMessSureLibrary?.isNotEmpty() == true) {
                        firstShipSelectedItem = CommonChooseListItem(
                            name = it.secondViolentTightPilot,
                            value = it.constantNovelistMessSureLibrary
                        )
                    }
                    inContact2st.apply {
                        tvRelation.text = it.expensiveSeriousPatientVoice
                        etPhone.text = SpannableStringBuilder(it.europeanBeardUniform)
                        etNom.text = SpannableStringBuilder(it.theseMedicalRadioactiveDoll)
                    }
                    if (it.expensiveSeriousPatientVoice?.isNotEmpty() == true && it.mostSpaceshipVideophoneBirdcage?.isNotEmpty() == true) {
                        secShipSelectedItem = CommonChooseListItem(
                            name = it.expensiveSeriousPatientVoice,
                            value = it.mostSpaceshipVideophoneBirdcage
                        )
                    }
                }
            }
        }
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

        mViewModel.saveContactSucLiveData.observe(this) {
            BaseConfirmCancelDialog.with(this)
                .content(getString(R.string.text_permission_declare))
                .confirm(getString(R.string.text_confirmer)) {
                    PermissionX.init(this)
                        .permissions(
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_CALENDAR,
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.READ_SMS,
                        )
                        .request { allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?>? ->
                            if (allGranted) {
                                mViewModel.jsonNeed()
                            } else {
                                BaseConfirmCancelDialog.with(this)
                                    .content(getString(R.string.text_afin_de_vous_fournir_un_bon_service_activez_les_autorisations_correspondantes_dans_les_param_tres_de_votre_t_l_phone))
                                    .confirm(getString(R.string.text_confirmer)) {
                                        val intent = Intent()

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            intent.action =
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                            val uri =
                                                Uri.fromParts("package", this.packageName, null)
                                            intent.data = uri
                                        } else {
                                            intent.action = Settings.ACTION_APPLICATION_SETTINGS
                                        }

                                        startActivity(intent)
                                    }.cancel(getString(R.string.text_fermer)) {

                                    }.build().show()
                            }
                        }
                }.cancel(getString(R.string.text_fermer)) {

                }.build().show()
        }

        mViewModel.jsonNeedUpLiveData.observe(this) {
            it?.let {
                if (it.agriculturalLidSingleFurnishedBush == "0") {
                    mViewModel.bigJsonUp()
                }
            }
        }

        mViewModel.jsonUpSucLiveData.observe(this) {
            startActivity(InputIdentityInformationActivity::class.java)
        }
    }

    private fun isNextBtnEnable(): Boolean {
        mBinding.tvNext.isSelected = InputUtil.nextBtnEnable(
            mBinding.inContact1st.tvRelation,
            mBinding.inContact1st.etPhone,
            mBinding.inContact1st.etNom,
            mBinding.inContact2st.tvRelation,
            mBinding.inContact2st.etPhone,
            mBinding.inContact2st.etNom
        )
        return mBinding.tvNext.isSelected
    }
}