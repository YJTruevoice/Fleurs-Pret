package com.facile.immediate.electronique.fleurs.pret.input.view.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.facile.immediate.electronique.fleurs.pret.R
import kotlin.math.max

class EmailTipsPopupWindow(context: Context, val itemClick: ((String) -> Unit)) :
    PopupWindow(context) {
    private val emailSuffixes =
        context.resources.getStringArray(R.array.string_arr_common_email_suffixes)
            .toMutableList()

    private val emailSuffixAdapter: EmailSuffixAdapter by lazy {
        EmailSuffixAdapter()
    }

    init {
        contentView = RecyclerView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layoutManager = LinearLayoutManager(context)
            adapter = emailSuffixAdapter.apply { filter(emailSuffixes) }
        }
        height = 200f.dp2px(context)
        setBackgroundDrawable(
            ContextCompat.getDrawable(context, R.drawable.shape_bg_ffffff_radius_6)
        )
    }

    fun updateAppend(curAppended: String) {
        val suffix = curAppended.substring(curAppended.indexOf("@"))
        val filterList = emailSuffixes.filter { it.startsWith(suffix) }.toMutableList()
        emailSuffixAdapter.filter(filterList)
    }

    inner class EmailSuffixAdapter : Adapter<VH>() {
        private val filterSuffixes = mutableListOf<String>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            return VH(TextView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
                gravity = Gravity.START
                setPadding(
                    6f.dp2px(context),
                    6f.dp2px(context),
                    6f.dp2px(context),
                    6f.dp2px(context)
                )

                setTextColor(ContextCompat.getColor(context, R.color.color_999999))
                textSize = 12f
            })
        }

        override fun getItemCount(): Int {
            return filterSuffixes.size
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.textView.text = filterSuffixes[position]
            holder.textView.setOnClickListener {
                itemClick.invoke(filterSuffixes[position])
                dismiss()
            }
        }

        fun filter(filterList: MutableList<String>) {
            val originSize = filterSuffixes.size
            val filterSize = filterList.size
            filterSuffixes.clear()
            filterSuffixes.addAll(filterList)
            notifyItemRangeChanged(0, max(originSize, filterSize))
        }

    }

    class VH(val textView: TextView) : ViewHolder(textView)
}