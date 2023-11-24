package com.facile.immediate.electronique.fleurs.pret.input.view

import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.BottomSheet
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.bean.CommonChooseListItem
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigType
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityInputInformationBinding
import com.facile.immediate.electronique.fleurs.pret.input.InputUtil
import com.facile.immediate.electronique.fleurs.pret.input.view.fragment.RegionDynamicLinkageFragment
import com.facile.immediate.electronique.fleurs.pret.input.view.fragment.SelectDateFragment
import com.facile.immediate.electronique.fleurs.pret.input.vm.BasicInputVM
import com.gyf.immersionbar.ImmersionBar

class InputInformationActivity :
    BaseMVVMActivity<ActivityInputInformationBinding, BasicInputVM>() {

    private var sexSelectedItem: CommonChooseListItem? = null
    private var regionCode: String = ""

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
            ConsumerActivity.go(this)
        }

        mBinding.etName.addTextChangedListener(mViewModel.textWatcher)
        mBinding.etNom.addTextChangedListener(mViewModel.textWatcher)
        mBinding.etEmail.addTextChangedListener(mViewModel.textWatcher)
        mBinding.etAddress.addTextChangedListener(mViewModel.textWatcher)

        mBinding.tvDate.setOnClickListener {
            val curDateStr = mBinding.tvDate.text.toString()
            val dateSplitArr = curDateStr.split("-").reversed()
            val dateParam = StringBuilder()
            for (i in dateSplitArr.indices) {
                dateParam.append(dateSplitArr[i])
                if (i < dateSplitArr.lastIndex) {
                    dateParam.append("-")
                }
            }
            SelectDateFragment.show(this, dateParam.toString()) { year, month, dayOfMonth ->
                val monthStr = if (month < 10) "0$month" else month
                val dayOfMonthStr = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
                mBinding.tvDate.text = "$dayOfMonthStr-$monthStr-$year"
                isNextBtnEnable()
            }
        }

        mBinding.tvSex.setOnClickListener {
            mViewModel.config(ConfigType.sex)//sex list
        }

        mBinding.tvRegion.setOnClickListener {
            RegionDynamicLinkageFragment.show(this) { province, city, district ->
                val builder = StringBuilder()
                if (province != null && province.normalAppointmentHeadmistressMachine.isNotEmpty()) {
                    builder.append(province.normalAppointmentHeadmistressMachine)
                }
                if (city != null && city.normalAppointmentHeadmistressMachine.isNotEmpty()) {
                    builder.append("-").append(city.normalAppointmentHeadmistressMachine)
                }
                if (district != null && district.normalAppointmentHeadmistressMachine.isNotEmpty()) {
                    builder.append("-").append(district.normalAppointmentHeadmistressMachine)
                }
                mBinding.tvRegion.text = builder.toString()
                isNextBtnEnable()
            }
        }

        mBinding.tvNext.setOnClickListener {
            if (!isNextBtnEnable()) {
                Toaster.showToast(AppKit.context.getString(R.string.veuilltext_ez_compl_ter_toutes_les_informations))
                return@setOnClickListener
            }
            mViewModel.savePersonalInfo(
                mBinding.etName.text.toString(),
                mBinding.etNom.text.toString(),
                mBinding.tvDate.text.toString(),
                sexSelectedItem?.value as String,
                mBinding.etEmail.text.toString(),
                "${mBinding.tvRegion.text} ${mBinding.etAddress.text}"
            )
        }
    }

    private fun initTitleBar() {
        mBinding.inTitleBar.tvTitle.text = getString(R.string.text_informations_personnelles)
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.userBasicLiveData.observe(this) {
            it?.let {
                mBinding.etName.setText(it.dirtyCrowdedEarthquakePrivateLevel)
                mBinding.etNom.setText(it.dustyChocolateEaster)
                mBinding.etEmail.setText(it.moralPressure)
                mBinding.etAddress.setText(it.dangerousGoatContraryDueSemicircle)
                mBinding.tvDate.text = it.messyChapterLemonDozen
                mBinding.tvSex.text =
                    InputUtil.sexMap(it.sureChemistryBigFairness ?: "")?.let { item ->
                        sexSelectedItem = item
                        item.name
                    }
                mBinding.tvRegion.text = ""
                isNextBtnEnable()
            }
        }

        mViewModel.textWatcherLiveData.observe(this) {
            isNextBtnEnable()
        }

        mViewModel.configLiveData.observe(this) { pair ->
            val list = mutableListOf<CommonChooseListItem>()
            pair?.second?.forEach {
                list.add(CommonChooseListItem(it.value, it.code))
            }
            BottomSheet.showListBottomSheet(this, list, selected = sexSelectedItem) {
                when (pair?.first) {
                    ConfigType.sex -> {
                        mBinding.tvSex.text = it.name
                        isNextBtnEnable()
                        sexSelectedItem = it
                    }

                    else -> {}
                }
            }
        }

        mViewModel.provinceLiveData.observe(this) { regions ->
            val list = mutableListOf<CommonChooseListItem>()
            regions?.forEach {
                list.add(
                    CommonChooseListItem(
                        it.normalAppointmentHeadmistressMachine,
                        it.eastBasicFavouriteSupermarket
                    )
                )
            }
            BottomSheet.showListBottomSheet(this, list) {
                mBinding.tvRegion.text = it.name
                isNextBtnEnable()
                regionCode = it.value as String
            }
        }
    }

    private fun isNextBtnEnable(): Boolean {
        mBinding.tvNext.isSelected = InputUtil.nextBtnEnable(
            mBinding.etName,
            mBinding.etNom,
            mBinding.etEmail,
            mBinding.etAddress,
            mBinding.tvDate,
            mBinding.tvSex,
            mBinding.tvRegion
        )
        return mBinding.tvNext.isSelected
    }

}