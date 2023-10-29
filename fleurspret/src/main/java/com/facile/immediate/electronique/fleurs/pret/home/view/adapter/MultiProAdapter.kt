package com.facile.immediate.electronique.fleurs.pret.home.view.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.arthur.commonlib.utils.DensityUtils
import com.arthur.commonlib.utils.image.DisplayUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.home.model.MultiP

class MultiProAdapter : BaseQuickAdapter<MultiP, QuickViewHolder>() {

    private var rankingIncrease = 1

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_multi_pro_list, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: MultiP?) {
        item?.apply {
            if (rudeReceptionCyclistArcticHunger.equals("0")) {
                if (rankingIncrease < 3) {
                    rankingSrc = when (rankingIncrease) {
                        1 -> R.mipmap.icon_1st_place
                        2 -> R.mipmap.icon_2st_place
                        else -> R.mipmap.icon_3st_place
                    }
                    rankingIncrease++
                }
            }
            holder.getView<ImageView>(R.id.iv_pro_logo).apply {
                DisplayUtils.displayImageAsRound(
                    chineseGeographyTenseHopefulSpirit,
                    this,
                    radius = DensityUtils.dp2px(context, 4f)
                )
            }

            holder.setText(R.id.tv_app_name, foggyEveryoneLivingArmyPrinter)
            holder.setText(R.id.tv_max_amount, afraidDecemberSlimClassicalTechnology)

            if (rankingSrc > 0) {
                holder.setImageResource(R.id.iv_ranking, rankingSrc)
            }

            holder.setText(R.id.tv_pro_consumer_entrance_btn, "Demander maintenant")
        }
    }
}