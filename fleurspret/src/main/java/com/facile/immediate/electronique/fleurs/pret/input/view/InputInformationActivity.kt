package com.facile.immediate.electronique.fleurs.pret.input.view

import android.Manifest
import android.content.DialogInterface
import android.widget.ArrayAdapter
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.BottomSheet
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.bean.CommonChooseListItem
import com.facile.immediate.electronique.fleurs.pret.common.EditTextFilter
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.config.CommonConfigItem
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigType
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.common.ext.onClick
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityInputInformationBinding
import com.facile.immediate.electronique.fleurs.pret.input.InputUtil
import com.facile.immediate.electronique.fleurs.pret.input.view.fragment.RegionDynamicLinkageFragment
import com.facile.immediate.electronique.fleurs.pret.input.view.fragment.SelectDateFragment
import com.facile.immediate.electronique.fleurs.pret.input.vm.BasicInputVM
import com.gyf.immersionbar.ImmersionBar
import com.permissionx.guolindev.PermissionX
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class InputInformationActivity :
    BaseMVVMActivity<ActivityInputInformationBinding, BasicInputVM>() {

    private var sexSelectedItem: CommonChooseListItem? = null

    private var showingDialog = AtomicBoolean(false)

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
        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            .request { _: Boolean, _: List<String?>?, _: List<String?>? ->

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

        mBinding.etName.addTextChangedListener(mViewModel.textWatcher)
        mBinding.etNom.addTextChangedListener(mViewModel.textWatcher)
        mBinding.etEmail.apply {
            addTextChangedListener(mViewModel.textWatcher)
            filters = arrayOf(EditTextFilter.getEmailEditFilter())

            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this@InputInformationActivity,
                android.R.layout.simple_dropdown_item_1line,
                resources.getStringArray(R.array.string_arr_common_email_suffixes)
            )
            setAdapter(adapter)
            setOnItemClickListener { parent, view, position, id ->
                val selected = parent.getItemAtPosition(position) as String
                append(selected)
            }
        }
        mBinding.etAddress.addTextChangedListener(mViewModel.textWatcher)

        mBinding.tvDate.onClick {
            val curDateStr = mBinding.tvDate.text.toString()
            val dateSplitArr = curDateStr.split("-").reversed()
            val dateParam = StringBuilder()
            for (i in dateSplitArr.indices) {
                dateParam.append(dateSplitArr[i])
                if (i < dateSplitArr.lastIndex) {
                    dateParam.append("-")
                }
            }
            if (showingDialog.get())return@onClick
            SelectDateFragment.show(this, dateParam.toString(),
                {
                    showingDialog.set(true)
                },
                {
                    showingDialog.set(false)
                }) { year, month, dayOfMonth ->
                val monthStr = if (month < 10) "0$month" else month
                val dayOfMonthStr = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
                mBinding.tvDate.text = "$dayOfMonthStr-$monthStr-$year"
                isNextBtnEnable()
            }
        }

        mBinding.tvSex.onClick {
            mViewModel.config(ConfigType.sex)//sex list
        }

        mBinding.tvRegion.onClick {
            if (showingDialog.get()) return@onClick
            RegionDynamicLinkageFragment.show(this, mViewModel.region,
                {
                    showingDialog.set(true)
                },
                {
                    showingDialog.set(false)
                }) { province, city, district ->
                if (province == null || city == null) return@show
                val builder = StringBuilder()
                if (province.normalAppointmentHeadmistressMachine.isNotEmpty()) {
                    builder.append(province.normalAppointmentHeadmistressMachine)
                    mViewModel.region = CommonConfigItem(
                        code = province.eastBasicFavouriteSupermarket,
                        value = province.normalAppointmentHeadmistressMachine
                    )
                }
                if (city.normalAppointmentHeadmistressMachine.isNotEmpty()) {
                    builder.append(" ").append(city.normalAppointmentHeadmistressMachine)
                    mViewModel.region?.next = CommonConfigItem(
                        code = city.eastBasicFavouriteSupermarket,
                        value = city.normalAppointmentHeadmistressMachine
                    )
                }
                if (district != null && district.normalAppointmentHeadmistressMachine.isNotEmpty()) {
                    builder.append(" ").append(district.normalAppointmentHeadmistressMachine)
                    mViewModel.region?.next?.next = CommonConfigItem(
                        code = district.eastBasicFavouriteSupermarket,
                        value = district.normalAppointmentHeadmistressMachine
                    )
                }
                mBinding.tvRegion.text = builder.toString()
                isNextBtnEnable()
            }
        }

        mBinding.tvNext.onClick {
            if (!isNextBtnEnable()) {
                Toaster.showToast(getString(R.string.veuilltext_ez_compl_ter_toutes_les_informations))
                return@onClick
            }
            mViewModel.savePersonalInfo(
                mBinding.etName.text.toString(),
                mBinding.etNom.text.toString(),
                mBinding.tvDate.text.toString(),
                sexSelectedItem?.value as? String ?: "",
                mBinding.etEmail.text.toString(),
                "${mBinding.etAddress.text}"
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
                mBinding.tvSex.text = it.properExperienceFlatSimilarBat
                if (!it.properExperienceFlatSimilarBat.isNullOrEmpty() && !it.sureChemistryBigFairness.isNullOrEmpty()) {
                    sexSelectedItem = CommonChooseListItem(
                        it.properExperienceFlatSimilarBat,
                        it.sureChemistryBigFairness
                    )
                }

                mViewModel.matchRegion(
                    it.mercifulVanillaMatchBitterFirewood,
                    it.neitherSeniorStocking,
                    it.australianHandsomeSummer
                )
                isNextBtnEnable()
            }
        }

        mViewModel.regionGetMatchLiveData.observe(this) {
            val region = mViewModel.region
            mBinding.tvRegion.text =
                "${region?.value ?: ""} ${region?.next?.value ?: ""} ${region?.next?.next?.value ?: ""}".trim()
            isNextBtnEnable()
        }

        mViewModel.textWatcherLiveData.observe(this) {
            isNextBtnEnable()
        }

        mViewModel.configLiveData.observe(this) { pair ->
            val list = mutableListOf<CommonChooseListItem>()
            pair?.second?.forEach {
                list.add(CommonChooseListItem(it.value, it.code))
            }
            if (showingDialog.get()) return@observe
            BottomSheet.showListBottomSheet(this, list, selected = sexSelectedItem,
                onShow = {
                    showingDialog.set(true)
                },
                onDismiss = {
                    showingDialog.set(false)
                }) {
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