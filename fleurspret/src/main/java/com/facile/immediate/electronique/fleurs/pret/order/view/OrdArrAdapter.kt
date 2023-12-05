package com.facile.immediate.electronique.fleurs.pret.order.view

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.arthur.commonlib.utils.image.DisplayUtils
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.event.HomeOrdState
import com.facile.immediate.electronique.fleurs.pret.common.ext.addThousandSeparator
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListDemandeRejeteeBinding
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListEnEvaluationBinding
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListQuitteBinding
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListRemboursementEnAttenteBinding
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListRetarde2JoursBinding
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListVersementEchoueBinding
import com.facile.immediate.electronique.fleurs.pret.input.view.InputGatheringInformationActivity
import com.facile.immediate.electronique.fleurs.pret.input.view.InputInformationActivity
import com.facile.immediate.electronique.fleurs.pret.order.model.Ord
import com.facile.immediate.electronique.fleurs.pret.order.model.OrdState
import org.greenrobot.eventbus.EventBus

class OrdArrAdapter : BaseMultiItemAdapter<Ord>() {

    init {
        addItemType(
            OrdState.EN_EVALUATION.value,
            object : OnMultiItemAdapterListener<Ord, EnEvaluationVH> {
                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): EnEvaluationVH {
                    return EnEvaluationVH(
                        ItemOrdListEnEvaluationBinding.inflate(
                            LayoutInflater.from(context),
                            parent,
                            false
                        )
                    )
                }

                override fun onBind(holder: EnEvaluationVH, position: Int, item: Ord?) {
                    item?.apply {
                        holder.binding.apply {
                            DisplayUtils.displayImageAsRound(
                                chineseGeographyTenseHopefulSpirit,
                                inOrdBasic.ivAppIcon,
                                radius = 4f.dp2px(context)
                            )
                            inOrdBasic.tvAppName.text = foggyEveryoneLivingArmyPrinter
                            inOrdBasic.tvOrdState.apply {
                                text = context.getString(R.string.text_en_valuation)
                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                                TextViewCompat.setCompoundDrawableTintList(
                                    this,
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.color_4635FF
                                        )
                                    )
                                )
                            }

                            inOrdMontant.inMontant.tvName.text =
                                context.getString(R.string.text_montant_du_pr_t)
                            inOrdMontant.inMontant.tvValue.apply {
                                text = "${context.getString(R.string.text_xof)} ${
                                    rudeHungryActionInformation.addThousandSeparator(2)
                                }"
                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                            }
                            inOrdMontant.inDate.tvName.text =
                                context.getString(R.string.text_date_de_demande)
                            inOrdMontant.inDate.tvValue.apply {
                                text = "$backSpeechMerchantFrightLeague"
                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                            }

                            if (theseMommyBroadHour == "1") {
                                tvPleaseWait.apply {
                                    visibility = View.VISIBLE
                                    text = comfortableMountain
                                    setOnClickListener {
                                        EventBus.getDefault().post(HomeOrdState())
                                    }
                                }
                            } else {
                                tvPleaseWait.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        ).addItemType(
            OrdState.VERSEMENT.value,
            object : OnMultiItemAdapterListener<Ord, EnEvaluationVH> {
                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): EnEvaluationVH {
                    return EnEvaluationVH(
                        ItemOrdListEnEvaluationBinding.inflate(
                            LayoutInflater.from(context),
                            parent,
                            false
                        )
                    )
                }

                override fun onBind(holder: EnEvaluationVH, position: Int, item: Ord?) {
                    item?.apply {
                        holder.binding.apply {
                            DisplayUtils.displayImageAsRound(
                                chineseGeographyTenseHopefulSpirit,
                                inOrdBasic.ivAppIcon,
                                radius = 4f.dp2px(context)
                            )
                            inOrdBasic.tvAppName.text = foggyEveryoneLivingArmyPrinter
                            inOrdBasic.tvOrdState.apply {
                                text = context.getString(R.string.text_en_train_de_versement)
                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                                TextViewCompat.setCompoundDrawableTintList(
                                    this,
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.color_4635FF
                                        )
                                    )
                                )
                            }

                            inOrdMontant.inMontant.tvName.text =
                                context.getString(R.string.text_montant_du_pr_t)
                            inOrdMontant.inMontant.tvValue.apply {
                                text = "${context.getString(R.string.text_xof)} ${
                                    rudeHungryActionInformation.addThousandSeparator(2)
                                }"
                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                            }
                            inOrdMontant.inDate.tvName.text =
                                context.getString(R.string.text_date_de_demande)
                            inOrdMontant.inDate.tvValue.apply {
                                text = "$backSpeechMerchantFrightLeague"
                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                            }

                            if (theseMommyBroadHour == "1") {
                                tvPleaseWait.apply {
                                    visibility = View.VISIBLE
                                    text = comfortableMountain
                                    setOnClickListener {
                                        EventBus.getDefault().post(HomeOrdState())
                                    }
                                }
                            } else {
                                tvPleaseWait.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        ).addItemType(
            OrdState.REMBOURSEMENT.value,
            object : OnMultiItemAdapterListener<Ord, RemboursementVH> {
                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): RemboursementVH {
                    return RemboursementVH(
                        ItemOrdListRemboursementEnAttenteBinding.inflate(
                            LayoutInflater.from(context),
                            parent,
                            false
                        )
                    )
                }

                override fun onBind(holder: RemboursementVH, position: Int, item: Ord?) {
                    item?.apply {
                        holder.binding.apply {
                            DisplayUtils.displayImageAsRound(
                                chineseGeographyTenseHopefulSpirit,
                                inOrdBasic.ivAppIcon,
                                radius = 4f.dp2px(context)
                            )
                            inOrdBasic.tvAppName.text = foggyEveryoneLivingArmyPrinter
                            inOrdBasic.tvOrdState.apply {
                                text = context.getString(R.string.text_remboursement_en_attente)
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
                            }

                            inOrdMontant.inMontant.tvName.text =
                                context.getString(R.string.text_montant_du_remboursement)
                            inOrdMontant.inMontant.tvValue.apply {
                                text = "${context.getString(R.string.text_xof)} ${
                                    eagerSculptureEasyPineShame.addThousandSeparator(2)
                                }"
                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                            }
                            inOrdMontant.inDate.tvName.text =
                                context.getString(R.string.text_date_de_remboursement)
                            inOrdMontant.inDate.tvValue.apply {
                                text = "$asleepEndlessEmptyNurse"
                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                            }

                            llBtns.visibility = View.GONE
                        }
                    }
                }
            }
        ).addItemType(
            OrdState.RETARDE.value,
            object : OnMultiItemAdapterListener<Ord, RetardeVH> {
                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): RetardeVH {
                    return RetardeVH(
                        ItemOrdListRetarde2JoursBinding.inflate(
                            LayoutInflater.from(context),
                            parent,
                            false
                        )
                    )
                }

                override fun onBind(holder: RetardeVH, position: Int, item: Ord?) {
                    item?.apply {
                        holder.binding.apply {
                            DisplayUtils.displayImageAsRound(
                                chineseGeographyTenseHopefulSpirit,
                                inOrdBasic.ivAppIcon,
                                radius = 4f.dp2px(context)
                            )
                            inOrdBasic.tvAppName.text = foggyEveryoneLivingArmyPrinter
                            inOrdBasic.tvOrdState.apply {
                                text = String.format(
                                    context.getString(R.string.text_retard_de_s_jours),
                                    energeticFightTobaccoSpade
                                )
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
                            }

                            inOrdMontant.inMontant.tvName.text =
                                context.getString(R.string.text_montant_du_remboursement)
                            inOrdMontant.inMontant.tvValue.apply {
                                text = "${context.getString(R.string.text_xof)} ${
                                    eagerSculptureEasyPineShame.addThousandSeparator(2)
                                }"
                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                            }
                            inOrdMontant.inDate.tvName.text =
                                context.getString(R.string.text_date_de_remboursement)
                            inOrdMontant.inDate.tvValue.apply {
                                text = "$asleepEndlessEmptyNurse"
                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                            }
                            if (theseMommyBroadHour == "1") {
                                llBtns.visibility = View.VISIBLE
                                tvDelayRemboursement.setOnClickListener {
                                    EventBus.getDefault().post(HomeOrdState())
                                }
                                tvRemboursement.setOnClickListener {
                                    EventBus.getDefault().post(HomeOrdState())
                                }
                            } else {
                                llBtns.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        ).addItemType(
            OrdState.REJETEE.value,
            object : OnMultiItemAdapterListener<Ord, RejeteeVH> {
                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): RejeteeVH {
                    return RejeteeVH(
                        ItemOrdListDemandeRejeteeBinding.inflate(
                            LayoutInflater.from(context),
                            parent,
                            false
                        )
                    )
                }

                override fun onBind(holder: RejeteeVH, position: Int, item: Ord?) {
                    item?.apply {
                        holder.binding.apply {
                            DisplayUtils.displayImageAsRound(
                                chineseGeographyTenseHopefulSpirit,
                                inOrdBasic.ivAppIcon,
                                radius = 4f.dp2px(context)
                            )
                            inOrdBasic.tvAppName.text = foggyEveryoneLivingArmyPrinter
                            inOrdBasic.tvOrdState.apply {
                                text = context.getString(R.string.text_la_demande_rejet_e)
                                setTextColor(ContextCompat.getColor(context, R.color.color_FF0000))
                                TextViewCompat.setCompoundDrawableTintList(
                                    this,
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.color_FF0000
                                        )
                                    )
                                )
                            }

                            if (theseMommyBroadHour == "1") {
                                tvVoirMaintenant.apply {
                                    visibility = View.VISIBLE
                                    setOnClickListener {
                                        EventBus.getDefault().post(HomeOrdState())
                                    }
                                }
                            } else {
                                tvVoirMaintenant.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        ).addItemType(
            OrdState.VERSEMENT_ECHOUE.value,
            object : OnMultiItemAdapterListener<Ord, VersementEchoueVH> {
                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): VersementEchoueVH {
                    return VersementEchoueVH(
                        ItemOrdListVersementEchoueBinding.inflate(
                            LayoutInflater.from(context),
                            parent,
                            false
                        )
                    )
                }

                override fun onBind(holder: VersementEchoueVH, position: Int, item: Ord?) {
                    item?.apply {
                        holder.binding.apply {
                            DisplayUtils.displayImageAsRound(
                                chineseGeographyTenseHopefulSpirit,
                                inOrdBasic.ivAppIcon,
                                radius = 4f.dp2px(context)
                            )
                            inOrdBasic.tvAppName.text = foggyEveryoneLivingArmyPrinter
                            inOrdBasic.tvOrdState.apply {
                                text = context.getString(R.string.text_versement_chou)
                                setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.color_FF0000
                                    )
                                )
                                TextViewCompat.setCompoundDrawableTintList(
                                    this,
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.color_FF0000
                                        )
                                    )
                                )
                            }

                            if (theseMommyBroadHour == "1") {
                                tvCompteDeReception.apply {
                                    visibility = View.VISIBLE
                                    setOnClickListener {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                InputGatheringInformationActivity::class.java
                                            ).apply {
                                                putExtra("ordId", normalBillClinicMercifulBay)
                                            })
                                    }
                                }
                            } else {
                                tvCompteDeReception.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        ).addItemType(
            OrdState.QUITTE.value,
            object : OnMultiItemAdapterListener<Ord, QuitteVH> {
                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): QuitteVH {
                    return QuitteVH(
                        ItemOrdListQuitteBinding.inflate(
                            LayoutInflater.from(context),
                            parent,
                            false
                        )
                    )
                }

                override fun onBind(holder: QuitteVH, position: Int, item: Ord?) {
                    item?.apply {
                        holder.binding.apply {
                            DisplayUtils.displayImageAsRound(
                                chineseGeographyTenseHopefulSpirit,
                                inOrdBasic.ivAppIcon,
                                radius = 4f.dp2px(context)
                            )
                            inOrdBasic.tvAppName.text = foggyEveryoneLivingArmyPrinter
                            inOrdBasic.tvOrdState.apply {
                                text = context.getString(R.string.text_quitte)
                                setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.color_10AC0F
                                    )
                                )
                                TextViewCompat.setCompoundDrawableTintList(
                                    this,
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.color_10AC0F
                                        )
                                    )
                                )
                            }

                            if (theseMommyBroadHour == "1") {
                                tvDemanderMaintenant.apply {
                                    visibility = View.VISIBLE
                                    setOnClickListener {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                InputInformationActivity::class.java
                                            )
                                        )
                                    }
                                }
                            } else {
                                tvDemanderMaintenant.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        ).onItemViewType { position, list ->
            val state = list[position].rudeReceptionCyclistArcticHunger
            if (state?.isDigitsOnly() == true) {
                state.toInt()
            } else {
                0
            }
        }

    }

    fun setData(ords: List<Ord>) {
        clear()
        addAll(ords)
    }

    private fun clear() {
        for (i in items) {
            remove(i)
        }
    }

    class EnEvaluationVH(
        val binding: ItemOrdListEnEvaluationBinding
    ) : ViewHolder(binding.root)

    class RemboursementVH(val binding: ItemOrdListRemboursementEnAttenteBinding) :
        ViewHolder(binding.root)

    class RetardeVH(val binding: ItemOrdListRetarde2JoursBinding) :
        ViewHolder(binding.root)

    class RejeteeVH(val binding: ItemOrdListDemandeRejeteeBinding) :
        ViewHolder(binding.root)

    class VersementEchoueVH(val binding: ItemOrdListVersementEchoueBinding) :
        ViewHolder(binding.root)

    class QuitteVH(val binding: ItemOrdListQuitteBinding) :
        ViewHolder(binding.root)
}