package com.facile.immediate.electronique.fleurs.pret.choosegold.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.ProdInfo

class RePayDateAdapter(private val itemClick: ((ProdInfo) -> Unit)? = null) :
    BaseQuickAdapter<ProdInfo, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_repayment_date_list, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: ProdInfo?) {
        item?.apply {
            holder.getView<TextView>(R.id.tv_repayment_date).apply {
                text = item.dateStr
                isSelected = item.selected
                isEnabled = !item.isLocked

                holder.itemView.setOnClickListener {
                    itemClick?.invoke(item)
                    if (isSelected || isLocked) return@setOnClickListener
                    for (i in items) {
                        i.selected =
                            i.neatPhysicsPeasantCommonSport == item.neatPhysicsPeasantCommonSport
                    }
                    notifyItemRangeChanged(0, itemCount)
                }
            }
            holder.getView<View>(R.id.iv_repayment_date_lock).apply {
                visibility = if (item.isLocked) View.VISIBLE else View.GONE
            }
        }
    }
}