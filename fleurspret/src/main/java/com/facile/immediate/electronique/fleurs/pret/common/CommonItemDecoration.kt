package com.facile.immediate.electronique.fleurs.pret.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px

class CommonItemDecoration(private val dividerWidth: Float = 14f) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.adapter?.let {
            if (parent.getChildAdapterPosition(view) < it.itemCount - 1) {
                outRect.bottom = dividerWidth.dp2px(view.context)
            }
        }
    }

}