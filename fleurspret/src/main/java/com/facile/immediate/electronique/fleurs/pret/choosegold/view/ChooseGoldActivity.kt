package com.facile.immediate.electronique.fleurs.pret.choosegold.view

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.recyclerview.widget.GridLayoutManager
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.ProdInfo
import com.facile.immediate.electronique.fleurs.pret.choosegold.view.adapter.RePayDateAdapter
import com.facile.immediate.electronique.fleurs.pret.choosegold.vm.ChooseGoldVM
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityChooseGoldInformationBinding

class ChooseGoldActivity : BaseMVVMActivity<ActivityChooseGoldInformationBinding, ChooseGoldVM>() {

    private val rePayDateAdapter: RePayDateAdapter by lazy {
        RePayDateAdapter() {
            displayBasicInfo(it)
        }
    }

    override fun buildView() {
        super.buildView()
        initTitleBar()

        PrivacyPolicyDisplayUtil.displayPrivacyPolicyGuide(this, mBinding.tvReadPrivacyPolicyGuide)
    }

    private fun initTitleBar() {
        mBinding.inTitleBar.tvTitle.text = getString(R.string.app_name)
    }

    override fun setListener() {
        super.setListener()
        mBinding.inTitleBar.ivBack.setOnClickListener {
            finish()
        }
        mBinding.inTitleBar.ivCustomer.setOnClickListener {

        }

        mBinding.tvApplyLoan.setOnClickListener {
            mViewModel.preOdr(
                neatPhysicsPeasantCommonSport = mViewModel.curProDate?.neatPhysicsPeasantCommonSport.toString(),
                rudeHungryActionInformation = mBinding.rbXofProgress.progress.toString()
            )
        }
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.prodListLiveData.observe(this) {
            it?.let {
                displayRepayDateList()
            }
        }
        mViewModel.preComputeResultLiveData.observe(this) {
            it?.let {
                mBinding.apply {
                    tvXofReceive.text = it.theoreticalMoneyLawyerSupper
                    tvXofInterest.text = it.unsafeAncestorBasement
                    tvXofService.text = it.unfairSeamanThemselvesMess
                    tvXofTva.text = it.crowdedSpecialistPunctuation
                    tvXofMontant.text = mBinding.rbXofProgress.progress.toString()

                    tvRepaymentGold.text = it.femaleSteamAsleepLifetime
                    tvRepaymentDate.text = mViewModel.curProDate?.dateStr
                }
            }
        }

        mViewModel.preOdrLiveData.observe(this) {

        }
    }

    private fun displayRepayDateList() {
        mBinding.rvRepaymentDate.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = rePayDateAdapter.apply {
                mViewModel.prodList?.bornSunglassesRipeProblemFalseHeadmaster?.let {
                    if (it.isNotEmpty()) {
                        addAll(it)
                        displayBasicInfo(it[0])
                    }
                }
            }
        }
        mBinding.tvApplyLoan.isSelected = true
    }

    private fun displayBasicInfo(prod: ProdInfo) {

        if (prod.isLocked) {
            // TODO:
            Toaster.showToast("dialog")
            return
        }

        mBinding.tvXof.text = "${prod.afraidDecemberSlimClassicalTechnology}"
        mBinding.tvXofDecrease.text = "-${prod.hugeFogPepper}"
        mBinding.tvXofIncrease.text = "+${prod.afraidDecemberSlimClassicalTechnology}"

        mBinding.rbXofProgress.apply {
            max = prod.afraidDecemberSlimClassicalTechnology
            progress = prod.afraidDecemberSlimClassicalTechnology

            setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (progress >= prod.hugeFogPepper) {
                        mBinding.tvXof.text = progress.toString()
                    } else {
                        mBinding.tvXof.text = prod.hugeFogPepper.toString()
                        seekBar?.progress = prod.hugeFogPepper
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.apply {
                        if (progress < prod.hugeFogPepper) {
                            mBinding.tvXof.text = prod.hugeFogPepper.toString()
                            progress = prod.hugeFogPepper
                        }
                    }
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.apply {
                        if (progress < prod.hugeFogPepper) {
                            mBinding.tvXof.text = prod.hugeFogPepper.toString()
                            progress = prod.hugeFogPepper
                        }
                    }
                    mViewModel.preCompute(
                        neatPhysicsPeasantCommonSport = prod.neatPhysicsPeasantCommonSport,
                        rudeHungryActionInformation = mBinding.rbXofProgress.progress.toString()
                    )
                }

            })
        }

        mViewModel.curProDate = prod

        mViewModel.preCompute(
            neatPhysicsPeasantCommonSport = prod.neatPhysicsPeasantCommonSport,
            rudeHungryActionInformation = mBinding.rbXofProgress.progress.toString()
        )
    }

}