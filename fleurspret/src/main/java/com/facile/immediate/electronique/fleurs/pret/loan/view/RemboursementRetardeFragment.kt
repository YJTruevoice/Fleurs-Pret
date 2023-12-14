package com.facile.immediate.electronique.fleurs.pret.loan.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthur.commonlib.utils.image.DisplayUtils
import com.arthur.network.TaskCollector
import com.arthur.network.ext.scopeMultiTaskLife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.AppConstants
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.CommonItemDecoration
import com.facile.immediate.electronique.fleurs.pret.common.ext.addThousandSeparator
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentLoanStateRemboursementLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseCountDownDialog
import com.facile.immediate.electronique.fleurs.pret.loan.model.OrdStateInfo
import com.facile.immediate.electronique.fleurs.pret.loan.model.PayDetail
import com.facile.immediate.electronique.fleurs.pret.loan.model.PayType
import com.facile.immediate.electronique.fleurs.pret.loan.model.ProState
import com.facile.immediate.electronique.fleurs.pret.loan.view.dialog.PayIntallmentDialog
import com.facile.immediate.electronique.fleurs.pret.main.MainActivity
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.utils.HighlightStrUtil
import com.facile.immediate.electronique.fleurs.pret.web.WebActivity

class RemboursementRetardeFragment :
    BaseLoanStateFragment<FragmentLoanStateRemboursementLayoutBinding>() {

    override fun setOrdInfo(ordInfo: OrdStateInfo) {
        //banner, 展期弹窗开关，展期弹窗标题，展期弹窗内容
        mViewModel.globalSetting("brownTopic,challengingDevelopmentTechnicalMineral,rainyIncomeUnhealthyPie,surroundingMedicineTriangleHarmlessHeadline")
        mViewModel.payList()
        mBinding.apply {
            inProInfo.apply {
                tvLoanState.apply {
                    text = when (mViewModel.proInfo?.rudeReceptionCyclistArcticHunger) {
                        ProState.REMBOURSEMENT.value.toString() -> {
                            setTextColor(ContextCompat.getColor(context, R.color.color_10AC0F))
                            TextViewCompat.setCompoundDrawableTintList(
                                this,
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.color_10AC0F
                                    )
                                )
                            )
                            tvStateTips.visibility = View.GONE
                            inMontantFrais.clRoot.visibility = View.GONE
                            inMontantTarif.clRoot.visibility = View.GONE

                            context.getString(R.string.text_remboursement_en_attente)
                        }

                        ProState.RETARDE.value.toString() -> {
                            setTextColor(ContextCompat.getColor(context, R.color.color_FFB02D))
                            TextViewCompat.setCompoundDrawableTintList(
                                this,
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.color_FFB02D
                                    )
                                )
                            )
                            tvStateTips.apply {
                                visibility = View.VISIBLE
                                val source = String.format(
                                    context.getString(R.string.text_votre_commande_est_en_retard),
                                    ordInfo.energeticFightTobaccoSpade
                                )
                                text = HighlightStrUtil.matchHighlight(
                                    ContextCompat.getColor(context, R.color.color_FF0000),
                                    source,
                                    ordInfo.energeticFightTobaccoSpade ?: ""
                                )
                            }
                            inMontantFrais.clRoot.visibility = View.VISIBLE
                            inMontantTarif.clRoot.visibility = View.VISIBLE

                            HighlightStrUtil.matchHighlight(
                                ContextCompat.getColor(context, R.color.color_FF0000),
                                String.format(
                                    context.getString(R.string.text_retard_de_s_jours),
                                    ordInfo.energeticFightTobaccoSpade
                                ),
                                ordInfo.energeticFightTobaccoSpade ?: ""
                            )
                        }

                        else -> {
                            ""
                        }
                    }
                }

                tvRepaymentGoldLabel.text = getString(R.string.text_montant_du_remboursement)
                tvRepaymentGold.text = ordInfo.eagerSculptureEasyPineShame.addThousandSeparator(2)
                tvRepaymentDateLabel.text = getString(R.string.text_date_du_remboursement)
                tvRepaymentDate.text = ordInfo.difficultTeapotNeedInitialPine


                inMontantRepayment.tvName.text = getString(R.string.text_montant_du_remboursement)
                inMontantRepayment.tvValue.text =
                    "XOF ${ordInfo.eagerSculptureEasyPineShame.addThousandSeparator(2)}"

                inMontantPret.tvName.text = getText(R.string.text_montant_du_pr_t)
                inMontantPret.tvValue.text =
                    "XOF ${ordInfo.friendlyBusinessmanHistory.addThousandSeparator(2)}"

                inMontantInteret.tvName.text = getText(R.string.text_int_r_t)
                inMontantInteret.tvValue.text =
                    "XOF ${ordInfo.unsafeAncestorBasement.addThousandSeparator(2)}"

                if (mViewModel.proInfo?.rudeReceptionCyclistArcticHunger == ProState.RETARDE.value.toString()) {
                    inMontantFrais.tvName.text = getString(R.string.text_frais_de_report)
                    inMontantFrais.tvValue.text =
                        "XOF ${ordInfo.hardworkingContraryPaddleLongHousework.addThousandSeparator(2)}"

                    inMontantTarif.tvName.text = getString(R.string.text_tarif_r_duit)
                    inMontantTarif.tvValue.text =
                        "XOF ${ordInfo.tightBoxFurniture.addThousandSeparator(2)}"
                }
            }

            btnProlongerRepayment.apply {
                if (ordInfo.maximumRealVinegarMadGentleman == "1") {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        // 查询还款详情&查询支付列表 -> 展期弹窗
                        scopeMultiTaskLife(
                            TaskCollector().apply {
                                put("payTypes") { mViewModel.payTypes() }
                                put("payDetail") { mViewModel.getPayDt() }
                            }
                        ).finisher { result ->
                            result?.let { map ->
                                (map["payDetail"] as? BaseResponse<PayDetail?>)?.let { resPD ->
                                    resPD.aggressiveParentMethod?.let { payd ->
                                        ac?.let { activity ->
                                            PayIntallmentDialog.with(activity)
                                                .title(context.getString(R.string.text_prolongation_des_d_lais))
                                                .content("")
                                                .ordId(
                                                    mViewModel.ordStateInfo?.normalBillClinicMercifulBay
                                                        ?: ""
                                                )
                                                .payDetail(payd)
                                                .payLinks(
                                                    (map["payTypes"] as? BaseResponse<List<PayType>?>)?.aggressiveParentMethod
                                                        ?: emptyList()
                                                )
                                                .build().show()
                                        }
                                    }
                                }
                            }

                        }.launch()
                    }
                } else {
                    visibility = View.GONE
                }
            }
        }
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.payTypeLiveData.observe(this) {
            it?.let {
                mBinding.rvRepaymentWay.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = object : BaseQuickAdapter<PayType, QuickViewHolder>() {
                        override fun onCreateViewHolder(
                            context: Context,
                            parent: ViewGroup,
                            viewType: Int
                        ): QuickViewHolder {
                            return QuickViewHolder(R.layout.layout_repayment_way_simple, parent)
                        }

                        override fun onBindViewHolder(
                            holder: QuickViewHolder,
                            position: Int,
                            item: PayType?
                        ) {
                            item?.apply {
                                DisplayUtils.displayImage(
                                    item.unfitCanadianSeat,
                                    holder.getView(R.id.iv_repayment_way),
                                    R.mipmap.icon_repayment_way
                                )

                                holder.setText(
                                    R.id.tv_repayment_way,
                                    item.someMidnightThisSentenceEnglishQuilt
                                )

                                holder.getView<View>(R.id.tv_payer).setOnClickListener {
                                    mViewModel.payLink(
                                        expensiveRadioGreyPetScientificSystem,
                                        someMidnightThisSentenceEnglishQuilt
                                    )
                                }
                            }
                        }
                    }.apply {
                        addAll(it)
                    }
                    if (itemDecorationCount <= 0) {
                        addItemDecoration(CommonItemDecoration(8f))
                    }
                }
            }

        }
        mViewModel.payLinkLiveData.observe(this) {
            it?.let {
                if (it.localLuckyHomeland == "1") {
                    // 跳转系统浏览器
                    if (it.eagerThunderstormCentigradeAmusementFairButterfly.isNotEmpty()) {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(it.eagerThunderstormCentigradeAmusementFairButterfly)
                        )
                        startActivity(intent)
                    }
                } else {
                    ac?.let { it1 ->
                        WebActivity.openForResult(
                            it1,
                            it.eagerThunderstormCentigradeAmusementFairButterfly,
                            REQUEST_CODE_REPAY,
                            getString(R.string.text_payer)
                        )
                    }
                }
            }
        }
    }

    override fun onGlobalSetting(setting: GlobalSetting) {
        mBinding.ivReloanGuide.apply {
            if (setting.brownTopic?.isNotEmpty() == true) {
                visibility = View.VISIBLE
                setOnClickListener {
                    WebActivity.open(context, setting.brownTopic)
                }
            } else {
                visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_REPAY
            && resultCode == Activity.RESULT_OK
            && data?.getStringExtra(AppConstants.KEY_WEB_VIEW_URL)?.isNotEmpty() == true
        ) {
            BaseCountDownDialog.with(requireContext())
                .countDown(5)
                .content(getString(R.string.text_vous_avez_rembours_le_pr_t_avec_succ_s))
                .confirm(getString(R.string.text_demander_un_nouveau_pr_t)) {
                    startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                        putExtra("selectedItemId", R.id.navigation_one)
                    })
                }
                .build().show()
        } else if (requestCode == REQUEST_CODE_REPAY_INTALLMENT
            && resultCode == Activity.RESULT_OK
            && data?.getStringExtra(AppConstants.KEY_WEB_VIEW_URL)?.isNotEmpty() == true
        ) {
            data.getBundleExtra(AppConstants.KEY_WEB_VIEW_BUNDLE)
                ?.getString("neitherFilmReligiousBuddhism")?.let { neitherFilmReligiousBuddhism ->
                    BaseCountDownDialog.with(requireContext())
                        .countDown(5)
                        .content(
                            HighlightStrUtil.matchHighlight(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.color_4635FF
                                ),
                                String.format(
                                    getString(R.string.text_votre_date_de_remboursement_final_a_t_modifi_e_s),
                                    neitherFilmReligiousBuddhism
                                ),
                                neitherFilmReligiousBuddhism
                            )
                        )
                        .confirm(getString(R.string.text_retourner)) {
                            it.dismiss()
                        }
                        .build().show()
                }

        }
    }

    companion object {
        const val REQUEST_CODE_REPAY = 0x1001
        const val REQUEST_CODE_REPAY_INTALLMENT = 0x1002
    }
}