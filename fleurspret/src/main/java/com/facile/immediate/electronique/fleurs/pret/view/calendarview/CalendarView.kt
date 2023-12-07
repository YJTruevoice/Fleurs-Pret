package com.facile.immediate.electronique.fleurs.pret.view.calendarview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.utils.DateUtil
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.ViewCalendarLayoutBinding
import java.util.Calendar

class CalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewCalendarLayoutBinding

    private var dateTarget: Calendar? = null
    private val yearsScope = mutableListOf<Int>()
    private var monthScope = mutableListOf<String>()
    private val monthNumMap = hashMapOf<String, Int>()

    private var curSelectedYear: Int = 0
    private var curSelectedMonth: Int = 0
    private var curSelectedDayOfMonth: Int = 0

    private val monthDaysAdapter: MonthDaysAdapter by lazy {
        MonthDaysAdapter()
    }

    init {
        binding = ViewCalendarLayoutBinding.inflate(LayoutInflater.from(context), this)

        dateTarget = generateDefaultDateTarget()
        generateYearsList()
        generateMonthNumMap()

        binding.spYear.apply {
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                yearsScope
            )
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    curSelectedYear = (parent?.adapter?.getItem(position) as? Int) ?: 0
                    monthDaysAdapter.exchangedYearOrMonth()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            setSelection(0)
        }
        monthScope = context.resources.getStringArray(R.array.string_arr_months)
            .toList() as MutableList<String>
        binding.spMonth.apply {
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                monthScope
            )
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val monthName = parent?.adapter?.getItem(position) as? String ?: ""
                    curSelectedMonth = monthNumMap[monthName] ?: 1
                    monthDaysAdapter.exchangedYearOrMonth()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            setSelection(0)
        }

        binding.rvMonthDays.apply {
            layoutManager = GridLayoutManager(context, 7)
            adapter = monthDaysAdapter
        }
    }

    private fun generateMonthNumMap() {
        context.resources.getStringArray(R.array.string_arr_months).forEachIndexed { index, s ->
            monthNumMap[s] = index + 1
        }
    }

    private fun generateYearsList() {
        val calendarDefault = DateUtil.getTarget("2000-01-01")
        curSelectedYear = dateTarget?.get(Calendar.YEAR) ?: calendarDefault.get(Calendar.YEAR)
        curSelectedMonth =
            (dateTarget?.get(Calendar.MONTH) ?: calendarDefault.get(Calendar.MONTH)) + 1
        curSelectedDayOfMonth =
            dateTarget?.get(Calendar.DAY_OF_MONTH) ?: calendarDefault.get(Calendar.DAY_OF_MONTH)

        val limitYear = DateUtil.getNow().get(Calendar.YEAR) - 10
        limitYear.let {
            yearsScope.add(it)
            for (i in 1..it - 1950) {
                yearsScope.add(it - i)
            }
        }
    }

    private fun initDisplay() {
        binding.spYear.apply {
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                yearsScope
            )
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    curSelectedYear = parent?.adapter?.getItem(position) as? Int ?: 0
                    monthDaysAdapter.exchangedYearOrMonth()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            setSelection(yearsScope.indexOf(curSelectedYear))
        }

        var selectionKey = ""
        monthNumMap.forEach {
            if (it.value == curSelectedMonth) {
                selectionKey = it.key
            }
        }

        binding.spMonth.setSelection(monthScope.indexOf(selectionKey))

        monthDaysAdapter.notifyChangeData()
    }

    fun setDateSource(source: String) {
        dateTarget = if (source.isEmpty()) {
            generateDefaultDateTarget()
        } else {
            DateUtil.getTarget(source)
        }
        generateYearsList()
        initDisplay()
    }

    private fun generateDefaultDateTarget(): Calendar {
        val calendarDefault = DateUtil.getTarget("2000-01-01")
        return DateUtil.getTarget(
            "${calendarDefault.get(Calendar.YEAR)}-${calendarDefault.get(Calendar.MONTH) + 1}-${
                calendarDefault.get(Calendar.DAY_OF_MONTH)
            }"
        )
    }

    fun getSelectedYear(): Int {
        return curSelectedYear
    }

    fun getSelectedMonth(): Int {
        return curSelectedMonth
    }

    fun getSelectedDayOfMonth(): Int {
        return curSelectedDayOfMonth
    }

    inner class MonthDaysAdapter : RecyclerView.Adapter<QuickViewHolder>() {

        private var weekDayInCurMonthFirstDay = 0
        private val days = arrayListOf<Int>()

        fun exchangedYearOrMonth() {
            notifyChangeData()
        }

        fun notifyChangeData() {
            weekDayInCurMonthFirstDay =
                SolarUtil.getFirstWeekOfMonth(curSelectedYear, curSelectedMonth)
            val monthDays = SolarUtil.getMonthDays(curSelectedYear, curSelectedMonth)
            Logger.logE(TAG, "curSelectedYear: $curSelectedYear")
            Logger.logE(TAG, "curSelectedMonth: $curSelectedMonth")
            Logger.logE(TAG, "weekDayInCurMonthFirstDay: $weekDayInCurMonthFirstDay")
            Logger.logE(TAG, "monthDays: $monthDays")
            val size = days.size
            days.clear()
            notifyItemRangeChanged(0, size)
            for (i in 0 until (weekDayInCurMonthFirstDay - 1)) {
                days.add(0)
            }
            for (i in 0 until monthDays) {
                days.add(i + 1)
            }

            notifyItemRangeChanged(0, days.size)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickViewHolder {
            return QuickViewHolder(R.layout.item_calendar_month_days_list, parent)
        }

        override fun getItemCount(): Int {
            return days.size
        }

        override fun onBindViewHolder(holder: QuickViewHolder, position: Int) {
            val item = days[position]
            item.apply {
                holder.getView<TextView>(R.id.tv_month_day).apply {
                    text = if (item >= 1) "$item" else ""

                    isSelected = curSelectedDayOfMonth > 0 && curSelectedDayOfMonth == item
                    if (isSelected) {
                        setTextColor(ContextCompat.getColor(context, R.color.color_F6F3FF))
                    } else {
                        if ((position % 7 == 0 || position % 7 == 6)) {
                            setTextColor(ContextCompat.getColor(context, R.color.color_999999))
                        } else {
                            setTextColor(ContextCompat.getColor(context, R.color.color_333333))
                        }
                    }

                    holder.getView<View>(R.id.fl_item).setOnClickListener {
                        if (item > 0 && curSelectedDayOfMonth != item) {
                            curSelectedDayOfMonth = item
                            notifyItemRangeChanged(0, itemCount)
                        }
                    }
                }

            }
        }
    }

    companion object {
        const val TAG = "CalendarView"
    }
}