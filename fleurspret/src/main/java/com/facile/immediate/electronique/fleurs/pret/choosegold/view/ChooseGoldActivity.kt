package com.facile.immediate.electronique.fleurs.pret.choosegold.view

import android.Manifest
import android.content.Intent
import android.os.Build
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
import com.facile.immediate.electronique.fleurs.pret.common.ext.addThousandSeparator
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityChooseGoldInformationBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseCountDownDialog
import com.facile.immediate.electronique.fleurs.pret.main.MainActivity
import com.permissionx.guolindev.PermissionX

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

    override fun processLogic() {
        super.processLogic()
        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            .request { _: Boolean, _: List<String?>?, _: List<String?>? ->

            }
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
                        "${getString(R.string.text_xof)} ${
                            it.theoreticalMoneyLawyerSupper.addThousandSeparator(
                                2
                            )
                        }"
                    tvXofInterest.text =
                        "${getString(R.string.text_xof)} ${
                            it.unsafeAncestorBasement.addThousandSeparator(
                                2
                            )
                        }"
                    tvXofService.text =
                        "${getString(R.string.text_xof)} ${
                            it.unfairSeamanThemselvesMess.addThousandSeparator(
                                2
                            )
                        }"
                    tvXofTva.text =
                        "${getString(R.string.text_xof)} ${
                            it.crowdedSpecialistPunctuation.addThousandSeparator(
                                2
                            )
                        }"
                    tvXofMontant.text =
                        "${getString(R.string.text_xof)} ${
                            mBinding.rbXofProgress.progress.toString().addThousandSeparator(2)
                        }"

                    tvRepaymentGold.text =
                        "${getString(R.string.text_xof)} ${
                            it.femaleSteamAsleepLifetime.addThousandSeparator(
                                2
                            )
                        }"
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
                .onCountDownFinish {
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        putExtra("selectedItemId", R.id.navigation_one)
                    })
                }
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

        mBinding.tvXof.text =
            prod.afraidDecemberSlimClassicalTechnology.toString().addThousandSeparator()
        mBinding.tvXofDecrease.text = "-${prod.hugeFogPepper.toString().addThousandSeparator()}"
        mBinding.tvXofIncrease.text =
            "+${prod.afraidDecemberSlimClassicalTechnology.toString().addThousandSeparator()}"

        mBinding.rbXofProgress.apply {
            max = prod.afraidDecemberSlimClassicalTechnology
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                min = prod.hugeFogPepper
            }
            progress = prod.afraidDecemberSlimClassicalTechnology
            stepSize = prod.backBenchRegularHomeland
            setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                val progress = ratingBar.progress
                if (progress >= prod.hugeFogPepper) {
                    mBinding.tvXof.text = progress.toString().addThousandSeparator()
                } else {
                    mBinding.tvXof.text = prod.hugeFogPepper.toString().addThousandSeparator()
                    ratingBar.progress = prod.hugeFogPepper
                }
                mViewModel.preCompute(
                    neatPhysicsPeasantCommonSport = prod.neatPhysicsPeasantCommonSport,
                    rudeHungryActionInformation = mBinding.rbXofProgress.progress.toString()
                )
            }
//            setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//                override fun onProgressChanged(
//                    seekBar: SeekBar?,
//                    progress: Int,
//                    fromUser: Boolean
//                ) {
//                    if (progress >= prod.hugeFogPepper) {
//                        mBinding.tvXof.text = progress.toString().addThousandSeparator()
//                    } else {
//                        mBinding.tvXof.text = prod.hugeFogPepper.toString().addThousandSeparator()
//                        seekBar?.progress = prod.hugeFogPepper
//                    }
//                }
//
//                override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                    seekBar?.apply {
//                        if (progress < prod.hugeFogPepper) {
//                            mBinding.tvXof.text = prod.hugeFogPepper.toString().addThousandSeparator()
//                            progress = prod.hugeFogPepper
//                        }
//                    }
//                }
//
//                override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                    seekBar?.apply {
//                        if (progress < prod.hugeFogPepper) {
//                            mBinding.tvXof.text = prod.hugeFogPepper.toString().addThousandSeparator()
//                            progress = prod.hugeFogPepper
//                        }
//                    }
//                    mViewModel.preCompute(
//                        neatPhysicsPeasantCommonSport = prod.neatPhysicsPeasantCommonSport,
//                        rudeHungryActionInformation = mBinding.rbXofProgress.progress.toString()
//                    )
//                }
//
//            })
        }

        mViewModel.curProDate = prod

        mViewModel.preCompute(
            neatPhysicsPeasantCommonSport = prod.neatPhysicsPeasantCommonSport,
            rudeHungryActionInformation = mBinding.rbXofProgress.progress.toString()
        )
    }

}