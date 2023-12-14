package com.facile.immediate.electronique.fleurs.pret.loan.view.dialog

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.image.DisplayUtils
import com.arthur.network.ext.scopeNetLife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.CommonItemDecoration
import com.facile.immediate.electronique.fleurs.pret.databinding.DialogPayIntallmentLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.BaseDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.CommonDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseDialog
import com.facile.immediate.electronique.fleurs.pret.loan.model.LoanAPI
import com.facile.immediate.electronique.fleurs.pret.loan.model.PayDetail
import com.facile.immediate.electronique.fleurs.pret.loan.model.PayType
import com.facile.immediate.electronique.fleurs.pret.loan.view.RemboursementRetardeFragment
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import com.facile.immediate.electronique.fleurs.pret.utils.HighlightStrUtil
import com.facile.immediate.electronique.fleurs.pret.web.WebActivity
import kotlinx.parcelize.Parcelize

open class PayIntallmentDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.CustomDialog
) : BaseDialog(context, themeResId) {

    private lateinit var mBinding: DialogPayIntallmentLayoutBinding


    override fun getContentView(): View {
        mBinding = DialogPayIntallmentLayoutBinding.inflate(LayoutInflater.from(context))
        return mBinding.root
    }

    override fun setDialogData(config: BaseDialogConfigEntity?) {
        (config as? PayIntallmentDialogConfigEntity)?.let {
            config.touchOutsideCancelAble = false
            // 公共初始化
            super.setDialogData(config)

            mBinding.ivClose.apply {
                setOnClickListener {
                    dismiss()
                }
            }

            mBinding.tvTitle.text = config.title

            val formatContent = String.format(
                context.getString(R.string.text_intallment_content),
                "${context.getString(R.string.text_xof)} ${config.payDetail?.eagerSculptureEasyPineShame}",
                config.payDetail?.neitherFilmReligiousBuddhism
            )

            mBinding.tvContent.text = HighlightStrUtil.matchHighlight(
                ContextCompat.getColor(context, R.color.color_FF0000), formatContent,
                listOf(
                    "${context.getString(R.string.text_xof)} ${config.payDetail?.eagerSculptureEasyPineShame}",
                    config.payDetail?.neitherFilmReligiousBuddhism?:""
                )
            )

            mBinding.inDateLimit.tvName.text =
                context.getString(R.string.text_la_date_limite_sera_prolong_e)
            mBinding.inDateLimit.tvValue.text = config.payDetail?.ripeCoffeePen

            mBinding.inFrais.tvName.text = context.getString(R.string.text_frais_de_report)
            mBinding.inFrais.tvValue.text = config.payDetail?.hardworkingContraryPaddleLongHousework

            mBinding.inInteret.tvName.text = context.getString(R.string.text_int_r_t)
            mBinding.inInteret.tvValue.text = config.payDetail?.primaryDustyMemory

            mBinding.inTva.tvName.text = context.getString(R.string.text_t_v_a)
            mBinding.inTva.tvValue.text = config.payDetail?.crowdedSpecialistPunctuation

            mBinding.inTarif.tvName.text = context.getString(R.string.text_tarif_r_duit)
            mBinding.inTarif.tvValue.text = config.payDetail?.tightBoxFurniture

            mBinding.inMontantPayer.tvName.text = context.getString(R.string.text_montant_payer)
            mBinding.inMontantPayer.tvValue.text = config.payDetail?.eagerSculptureEasyPineShame


            mBinding.rvPayerType.apply {
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
                            holder.getView<View>(R.id.cl_root).background =
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.shape_bg_ffffff_radius_6
                                )
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
                                (this@PayIntallmentDialog.context as? FragmentActivity)?.let { activity ->
                                    activity.scopeNetLife {
                                        NetMgr.get().service<LoanAPI>().getPayLink(
                                            config.ordId,
                                            "01",
                                            expensiveRadioGreyPetScientificSystem
                                        )
                                    }.success {
                                        if (it.aggressiveParentMethod?.eagerThunderstormCentigradeAmusementFairButterfly.isNullOrBlank()) {
                                            Toaster.showToast(context.getString(R.string.text_mauvais_lien_de_paiement))
                                            return@success
                                        }
                                        WebActivity.openForResult(
                                            activity,
                                            it.aggressiveParentMethod?.eagerThunderstormCentigradeAmusementFairButterfly
                                                ?: "",
                                            RemboursementRetardeFragment.REQUEST_CODE_REPAY_INTALLMENT,
                                            bundle = Bundle().apply {
                                                putString(
                                                    "neitherFilmReligiousBuddhism",
                                                    config.payDetail?.neitherFilmReligiousBuddhism
                                                )
                                            }
                                        )
                                        dismiss()
                                    }.launch()
                                }
                            }
                        }
                    }
                }.apply {
                    addAll(config.payLinks)
                }
                if (itemDecorationCount <= 0) {
                    addItemDecoration(CommonItemDecoration(8f))
                }
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        config?.confirmCallback?.invoke(this@PayIntallmentDialog)
    }

    companion object {
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    class Builder constructor(var context: Context) : BaseDialog.Builder<Builder>(context) {
        override fun createDialog(): BaseDialog {
            return PayIntallmentDialog(context).apply {
                setOwnerActivity(context as FragmentActivity)
            }
        }

        override fun createConfig(): BaseDialogConfigEntity {
            return PayIntallmentDialogConfigEntity()
        }

        fun ordId(ordId: String): Builder {
            (config as? PayIntallmentDialogConfigEntity)?.ordId = ordId
            return this
        }

        fun payDetail(payDetail: PayDetail): Builder {
            (config as? PayIntallmentDialogConfigEntity)?.payDetail = payDetail
            return this
        }

        fun payLinks(payLinks: List<PayType>): Builder {
            (config as? PayIntallmentDialogConfigEntity)?.payLinks = payLinks
            return this
        }

        fun content(content: String): Builder {
            (config as? PayIntallmentDialogConfigEntity)?.content = content
            return this
        }
    }

    @Parcelize
    class PayIntallmentDialogConfigEntity(
        var ordId: String = "",
        var payDetail: PayDetail? = null,
        var payLinks: List<PayType> = emptyList(),
    ) : CommonDialogConfigEntity(), Parcelable

}