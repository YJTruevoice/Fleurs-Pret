package com.facile.immediate.electronique.fleurs.pret.choosegold.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.Toaster
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.ProdInfo
import com.facile.immediate.electronique.fleurs.pret.choosegold.view.adapter.RePayDateAdapter
import com.facile.immediate.electronique.fleurs.pret.choosegold.view.dialog.LockedRepaymentDateTip
import com.facile.immediate.electronique.fleurs.pret.choosegold.view.dialog.PreOrdDialog
import com.facile.immediate.electronique.fleurs.pret.choosegold.vm.ChooseGoldVM
import com.facile.immediate.electronique.fleurs.pret.common.CommonItemDecoration
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.config.CommonConfigItem
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
            ConsumerActivity.goBranch(this)
        }

        mBinding.tvApplyLoan.setOnClickListener {
            if (mViewModel.curProDate == null) {
                Toaster.showToast(getString(R.string.text_seleccione_el_plazo_del_pr_stamo))
                return@setOnClickListener
            }
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
                mBinding.tvApplyLoan.isSelected = true
            }

            mBinding.apply {
                tvXofReceive.text =
                    "${getString(R.string.text_xof)} ${
                        it?.theoreticalMoneyLawyerSupper?.let { theoreticalMoneyLawyerSupper ->
                            theoreticalMoneyLawyerSupper.addThousandSeparator(2)
                        } ?: "--"
                    }"

                rvDetails.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = object : BaseQuickAdapter<CommonConfigItem, QuickViewHolder>() {
                        override fun onCreateViewHolder(
                            context: Context,
                            parent: ViewGroup,
                            viewType: Int
                        ): QuickViewHolder {
                            return QuickViewHolder(R.layout.layout_information_between, parent)
                        }

                        override fun onBindViewHolder(
                            holder: QuickViewHolder,
                            position: Int,
                            item: CommonConfigItem?
                        ) {
                            item?.apply {
                                holder.setText(R.id.tv_name, item.code)
                                holder.getView<TextView>(R.id.tv_value).apply {
                                    setTextColor(
                                        ContextCompat.getColor(context, R.color.color_4635FF)
                                    )
                                    text = "${getString(R.string.text_xof)} ${
                                        item.value.addThousandSeparator(2)
                                    }"
                                }
                            }
                        }

                    }.apply {
                        addAll(mutableListOf(
                            CommonConfigItem(
                                getString(R.string.text_int_r_t),
                                it?.unsafeAncestorBasement ?: "--"
                            ),
                            CommonConfigItem(
                                getString(R.string.text_frais_de_service),
                                it?.unfairSeamanThemselvesMess ?: "--"
                            ),
                            CommonConfigItem(
                                getString(R.string.text_t_v_a),
                                it?.crowdedSpecialistPunctuation ?: "--"
                            ),
                        ).apply {
                            it?.finalStorageGlove?.let { finalStorageGlove ->
                                for (e in finalStorageGlove) {
                                    this.add(
                                        CommonConfigItem(
                                            e.irishGradeUndergroundAmericanPostcard,
                                            e.peacefulFancySlightTeam
                                        )
                                    )
                                }
                            }
                        })
                    }
                    if (itemDecorationCount <= 0) {
                        addItemDecoration(CommonItemDecoration(8f))
                    }
                }

                tvXofMontant.text =
                    "${getString(R.string.text_xof)} ${
                        mBinding.rbXofProgress.progress.toString().addThousandSeparator(2)
                    }"

                tvRepaymentGold.text =
                    "${getString(R.string.text_xof)} ${
                        it?.femaleSteamAsleepLifetime?.let { femaleSteamAsleepLifetime ->
                            femaleSteamAsleepLifetime.addThousandSeparator(2)
                        } ?: "--"
                    }"
                tvRepaymentDate.text = mViewModel.curProDate?.dateStr ?: "--"
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
                        var defaultSelected: ProdInfo? = null
                        for (e in it) {
                            if (e.selected) {
                                defaultSelected = e
                            }
                        }
                        defaultSelected?.let {
                            displayBasicInfo(defaultSelected)
                        } ?: displayBasicInfo(it[0], true)
                    }
                }
            }
        }
    }

    private fun displayBasicInfo(prod: ProdInfo, defaultSelectedFist: Boolean = false) {

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
            setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    seekBar?.apply {
                        val step = prod.backBenchRegularHomeland.toInt() // 设置步长
                        val remainder = progress % step

                        val newProgress: Int = if (remainder < step / 2) {
                            progress - remainder
                        } else {
                            progress + (step - remainder)
                        }

                        mBinding.tvXof.text = newProgress.toString().addThousandSeparator()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.apply {
                        val step = prod.backBenchRegularHomeland.toInt() // 设置步长

                        val progress = this.progress
                        val remainder = progress % step

                        val newProgress: Int = if (remainder < step / 2) {
                            progress - remainder
                        } else {
                            progress + (step - remainder)
                        }

                        mBinding.tvXof.text = newProgress.toString().addThousandSeparator()
                        if (newProgress >= prod.hugeFogPepper) {
                            mBinding.tvXof.text = newProgress.toString().addThousandSeparator()
                        } else {
                            mBinding.tvXof.text =
                                prod.hugeFogPepper.toString().addThousandSeparator()
                        }
                        this.progress = newProgress
                        if (mViewModel.curProDate != null) {
                            mViewModel.preCompute(
                                neatPhysicsPeasantCommonSport = prod.neatPhysicsPeasantCommonSport,
                                rudeHungryActionInformation = newProgress.toString()
                            )
                        }
                    }
                }
            })
        }

        mViewModel.curProDate = if (defaultSelectedFist) null else prod
        if (mViewModel.curProDate != null) {
            mViewModel.preCompute(
                neatPhysicsPeasantCommonSport = prod.neatPhysicsPeasantCommonSport,
                rudeHungryActionInformation = mBinding.rbXofProgress.progress.toString()
            )
        } else {
            mViewModel.preComputeResultLiveData.value = null
        }
    }

}