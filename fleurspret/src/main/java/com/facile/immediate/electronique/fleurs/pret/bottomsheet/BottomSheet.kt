package com.facile.immediate.electronique.fleurs.pret.bottomsheet

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.arthur.commonlib.utils.ValuesUtils
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.bean.CommonChooseListItem
import com.facile.immediate.electronique.fleurs.pret.common.CommonItemDecoration
import com.facile.immediate.electronique.fleurs.pret.databinding.DialogNcBottomsheetListViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

object BottomSheet {

    fun showListBottomSheet(
        ac: Activity,
        data: List<CommonChooseListItem>,
        selected: CommonChooseListItem? = null,
        textCenter: Boolean = true,
        textBold: Boolean = false,
        textIncludePadding: Boolean = true,
        cancelCallback: (() -> Unit)? = null,
        callback: (CommonChooseListItem) -> Unit
    ) {
        val view = LayoutInflater.from(ac).inflate(R.layout.dialog_nc_bottomsheet_list_view, null)
        val mBinding = DialogNcBottomsheetListViewBinding.bind(view)
        val bottomSheetDialog = BottomSheetDialog(ac, R.style.BottomSheetDialog)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as ViewGroup)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetDialog.dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.rv_dialog_bottom_sheet_list)
        recyclerView.layoutManager = LinearLayoutManager(ac)
        recyclerView.addItemDecoration(CommonItemDecoration())

        var selectedItem: CommonChooseListItem? = selected
        recyclerView.adapter =
            ListBottomSheetAdapter(ac, textCenter, textBold, textIncludePadding) {
//                bottomSheetDialog.dismiss()
            }.apply {
                setData(data, selected) {
                    selectedItem = it
                }
            }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        mBinding.tvDialogBottomSheetClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        mBinding.tvDialogBottomSheetConfirm.setOnClickListener {
            selectedItem?.let { it1 ->
                callback.invoke(it1)
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setOnCancelListener {
            cancelCallback?.invoke()
        }
        bottomSheetDialog.show()
    }

    private class ListBottomSheetAdapter(
        val context: Context,
        val textCenter: Boolean,
        val textBold: Boolean,
        val textIncludePadding: Boolean,
        val dismissCallback: (() -> Unit)? = null
    ) :
        RecyclerView.Adapter<ListBottomSheetAdapter.ListBottomSheetViewHolder>() {

        private val mDataList = ArrayList<CommonChooseListItem>(10)

        private var mSelected: CommonChooseListItem? = null

        private var callback: ((CommonChooseListItem) -> Unit)? = null

        fun setData(
            data: List<CommonChooseListItem>,
            selected: CommonChooseListItem? = null,
            callback: (CommonChooseListItem) -> Unit
        ) {
            mDataList.clear()
            mDataList.addAll(data)
            mSelected = selected
            this.callback = callback
            mDataList.forEach {
                it.selected = this.mSelected?.equals(it) == true
            }
            val selectedItem = mDataList.find { it.selected }
            if (selectedItem == null) {
                mDataList.first().selected = true
                callback.invoke(mDataList.first())
            }
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return mDataList.size
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ListBottomSheetViewHolder {
            val frameLayout = FrameLayout(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                background =
                    ValuesUtils.getDrawableById(
                        R.drawable.drawable_bg_state_sheet_list_item,
                        context
                    )
            }

            return ListBottomSheetViewHolder(frameLayout)
        }

        override fun onBindViewHolder(holder: ListBottomSheetViewHolder, position: Int) {
            val item = mDataList[position]
            holder.view.removeAllViews()
            holder.view.setPadding(
                4f.dp2px(context),
                10f.dp2px(context),
                4f.dp2px(context),
                10f.dp2px(context)
            )
            val tv = TextView(context).apply {
                textSize = 18f
                paint.isFakeBoldText = (textBold || item.fakeBold)
                maxLines = 2
                ellipsize = item.nameEllipsize
                setPadding(8f.dp2px(context))
            }
            holder.view.addView(tv)
            tv.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = if (textCenter) Gravity.CENTER else Gravity.CENTER_VERTICAL
            }
            tv.compoundDrawablePadding = 4f.dp2px(context)
            tv.setCompoundDrawables(null, null, null, null)
            item.drawableStart?.let {
                it.setBounds(
                    0,
                    0,
                    tv.textSize.toInt() * (it.intrinsicWidth / it.intrinsicHeight),
                    tv.textSize.toInt()
                )
                tv.setCompoundDrawables(it, null, null, null)
            }
            tv.text = item.name
            if (item.selected) {
                tv.setTextColor(
                    ValuesUtils.getColor(
                        R.color.color_4635FF,
                        context
                    )
                )
            } else {
                tv.setTextColor(
                    ValuesUtils.getColor(
                        if (item.weak) R.color.color_D8D8D8
                        else R.color.color_999999, context
                    )
                )
            }
            item.rightDrawable?.let {
                it.setBounds(
                    0,
                    0,
                    tv.textSize.toInt() * (it.intrinsicWidth / it.intrinsicHeight),
                    tv.textSize.toInt()
                )
                val img = ImageView(context)
                img.setImageDrawable(it)
                holder.view.addView(img)
                img.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER_VERTICAL or Gravity.END
                }
            }
            holder.view.isSelected = item.selected
            holder.view.setOnClickListener {
                dismissCallback?.invoke()
                callback?.invoke(item)
                this.mSelected = item
                mDataList.forEach {
                    it.selected = this.mSelected == it
                }
                notifyItemRangeChanged(0, mDataList.size)
            }
        }

        class ListBottomSheetViewHolder(val view: FrameLayout) : RecyclerView.ViewHolder(view)

    }


}