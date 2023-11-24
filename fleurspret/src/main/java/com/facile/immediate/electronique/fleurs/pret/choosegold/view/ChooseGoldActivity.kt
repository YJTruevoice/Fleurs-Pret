package com.facile.immediate.electronique.fleurs.pret.choosegold.view

import android.content.Intent
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.recyclerview.widget.GridLayoutManager
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.ProdInfo
import com.facile.immediate.electronique.fleurs.pret.choosegold.view.adapter.RePayDateAdapter
import com.facile.immediate.electronique.fleurs.pret.choosegold.view.dialog.LockedRepaymentDateTip
import com.facile.immediate.electronique.fleurs.pret.choosegold.view.dialog.PreOrdDialog
import com.facile.immediate.electronique.fleurs.pret.choosegold.vm.ChooseGoldVM
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityChooseGoldInformationBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseCountDownDialog
import com.facile.immediate.electronique.fleurs.pret.main.MainActivity

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
            ConsumerActivity.go(this)
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
                    tvXofReceive.text =
                        "${getString(R.string.text_xof)} ${it.theoreticalMoneyLawyerSupper}"
                    tvXofInterest.text =
                        "${getString(R.string.text_xof)} ${it.unsafeAncestorBasement}"
                    tvXofService.text =
                        "${getString(R.string.text_xof)} ${it.unfairSeamanThemselvesMess}"
                    tvXofTva.text =
                        "${getString(R.string.text_xof)} ${it.crowdedSpecialistPunctuation}"
                    tvXofMontant.text =
                        "${getString(R.string.text_xof)} ${mBinding.rbXofProgress.progress}"

                    tvRepaymentGold.text =
                        "${getString(R.string.text_xof)} ${it.femaleSteamAsleepLifetime}"
                    tvRepaymentDate.text = mViewModel.curProDate?.dateStr
                }
            }
        }

        mViewModel.preOdrLiveData.observe(this) {
            PreOrdDialog.with(
                this,
                mBinding.tvXofReceive.text.toString(),
                mBinding.tvRepaymentGold.text.toString(),
                mBinding.tvRepaymentDate.text.toString()
            ).title(getString(R.string.text_d_tails))
                .confirm(getString(R.string.text_confirmer)) {
                    mViewModel.submitOrd(
                        neatPhysicsPeasantCommonSport = mViewModel.curProDate?.neatPhysicsPeasantCommonSport.toString(),
                        rudeHungryActionInformation = mBinding.rbXofProgress.progress.toString()
                    )
                }.cancel(getString(R.string.text_annuler)) {

                }.build().show()
        }

        mViewModel.submitOdrLiveData.observe(this) {
            BaseCountDownDialog.with(this)
                .img(R.mipmap.pic_submit_success)
                .content(getString(R.string.text_votre_pr_t_a_t_demand_avec_succ_s))
                .confirm(getString(R.string.text_ok)) {
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        putExtra("selectedItemId", R.id.navigation_one)
                    })
                }
                .countDown(3)
                .build().show()
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
            LockedRepaymentDateTip.with(this).build().show()
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