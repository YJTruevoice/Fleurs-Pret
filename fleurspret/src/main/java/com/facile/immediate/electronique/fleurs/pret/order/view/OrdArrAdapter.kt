package com.facile.immediate.electronique.fleurs.pret.order.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListDemandeRejeteeBinding
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListEnEvaluationBinding
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListQuitteBinding
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListRemboursementEnAttenteBinding
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListRetarde2JoursBinding
import com.facile.immediate.electronique.fleurs.pret.databinding.ItemOrdListVersementEchoueBinding
import com.facile.immediate.electronique.fleurs.pret.order.model.Ord
import com.facile.immediate.electronique.fleurs.pret.order.model.OrdState

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

                        }
                    }
                }
            }
        )
            .onItemViewType { position, list ->
                val state = list[position].rudeReceptionCyclistArcticHunger
                if (state?.isDigitsOnly() == true) {
                    state.toInt()
                } else {
                    0
                }
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