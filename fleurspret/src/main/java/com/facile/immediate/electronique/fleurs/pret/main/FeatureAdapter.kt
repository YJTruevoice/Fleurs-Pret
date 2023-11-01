package com.facile.immediate.electronique.fleurs.pret.main

import android.content.Context
import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.R

class FeatureAdapter : BaseQuickAdapter<FeatureEntity, QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: FeatureEntity?) {
        item?.apply {
            holder.getView<ViewGroup>(R.id.item_root).backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, item.backgroundTint))
            holder.setImageResource(R.id.iv_unique_feature, item.featureImgSrc)
            holder.setText(R.id.tv_unique_feature, context.resources.getString(item.featureTextId))
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_unique_feature_layout, parent)
    }
}