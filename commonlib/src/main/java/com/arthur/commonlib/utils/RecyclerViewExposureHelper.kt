package com.arthur.commonlib.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.FloatRange
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import java.util.*

/**
 * recyclerView曝光监听
 *
 * 列表刷新/插入/删除后会借助[AdapterDataObserver]重新计算曝光，无需外层处理
 * 列表宿主可见/不可见情况通过设置[LifecycleOwner]监听其声明周期，并以onResume/onPause作为标准重新曝光/结束曝光
 *
 * 同时提供了[onReVisible]、[onInvisible]方法给调用方自行控制无生命周期变化的遮挡情况下的曝光
 * 但一定要注意成对调用，不调用[onReVisible]后续的滑动不会再进行曝光
 *
 * item的根布局view需要将数据源对象设为其tag，做为条目唯一性的判断，并在回调中返回用以曝光上报
 * 如果不设置会默认以position判断，但此时刷新，插入，删除操作后会有当前几个item判断不准的情况出现
 * 所以
 * 设itemView.tag
 * 设itemView.tag
 * 设itemView.tag
 *
 * @param recyclerView
 * @param ratio 漏出多少算有效曝光 (0f, 1f]
 * @param idle 是否停留松手后才算一次有效曝光
 * @param lifecycleOwner 当前rv宿主，赋值则会根据onResume/onPause判定页面是否可见来恢复/结束曝光
 * @param callback 曝光回调（item[ExposureItemData], 当前是曝光/结束曝光）
 */
class RecyclerViewExposureHelper(
    private val recyclerView: RecyclerView,
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false) private val ratio: Float = 1f,
    private val idle: Boolean = false,
    private val lifecycleOwner: LifecycleOwner? = null,
    private val callback: (item: ExposureItemData, visible: Boolean) -> Unit
) {

    /**
     * 用于调用getLocalVisibleRect获取view漏出尺寸，全局使用一个对象即可，避免多次创建在这里初始化
     */
    private val rect = Rect()

    /**
     * 用来标记当前在屏幕中的position，曝光时加入，结束曝光移除
     */
    private val exposureDataSet: HashSet<ExposureItemData> = HashSet(8)

    /**
     * 计算曝光位置时用到的临时set，取得当前真正曝光的条目与[exposureDataSet]做对比得出结果
     */
    private val exposureDataTempSet: HashSet<ExposureItemData> = HashSet(8)

    /**
     * 宿主当前是否可见，不可见时不进行曝光
     */
    private var pageVisibleNow = true

    init {
        val adapter = recyclerView.adapter
        assert(adapter != null) { "请先设置adapter" }
        //列表刷新/插入/删除会触发这个
        adapter?.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                onChange(ExposureTypeEnum.REFRESH)
            }

            //下面这几个方法参数参考意义不大，统一调到onChange里
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                onChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            fun onChange(type: ExposureTypeEnum) {
                recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        calculateExposure(type)
                    }

                })
            }

        })

        recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    calculateExposure(ExposureTypeEnum.SCROLL)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //仅滑动曝光场景并且有滑动距离才去计算
                if (!idle && (dx != 0 || dy != 0)) {
                    calculateExposure(ExposureTypeEnum.SCROLL)
                }
            }
        })

        //以onResume/onPause为标志判断当前宿主是否可见，不可见时结束曝光，再次可见时重新计算曝光
        lifecycleOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                onReVisible()
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                onInvisible()
            }

        })

    }

    /**
     * 页面重新可见，计算曝光
     */
    fun onReVisible() {
        pageVisibleNow = true
        calculateExposure(ExposureTypeEnum.COVER)
    }

    /**
     * 页面不可见，结束曝光
     */
    fun onInvisible() {
        pageVisibleNow = false
        exposureDataSet.forEach {
            exposureCallback(it, ExposureTypeEnum.COVER, false)
        }
        exposureDataSet.clear()
    }

    /**
     * 计算曝光位置，post防止更新延迟和异步问题
     */
    private fun calculateExposure(type: ExposureTypeEnum) {
        if (pageVisibleNow) {
            recyclerView.post {
                try {
                    realCalculate(type)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 计算部分，不要直接调，通过[calculateExposure]调用
     */
    private fun realCalculate(type: ExposureTypeEnum) {
        exposureDataTempSet.clear()
        for (index in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(index)
            val position = recyclerView.getChildAdapterPosition(child)
            //此时处于刷新中状态，不做考虑
            if (position == RecyclerView.NO_POSITION) {
                continue
            }
            //当前是否可见
            val isVisible = child.getLocalVisibleRect(rect)
            //可见区域尺寸
            val childVisibleArea = getChildVisibleArea(recyclerView)
            //子view总尺寸
            val childTotalArea = getChildTotalArea(recyclerView, child)

            //当前条目可见并且可视区域大于曝光阈值，将其添加到临时set中，遍历结束后和exposureDataSet比较
            if (isVisible && childVisibleArea >= childTotalArea * ratio) {
                exposureDataTempSet.add(
                    ExposureItemData(
                        //没有设置tag的话用position代替，但是会在列表数据变动的情况下出现不准的情况，强烈建议设置tag
                        child.tag ?: position,
                        position
                    )
                )
            }
        }
        //找出需要曝光的条目
        exposureDataTempSet.filter { exposureDataSet.add(it) }
            .forEach { exposureCallback(it, type, true) }
        //找出结束曝光的条目
        val removedItems = exposureDataSet.filter { !exposureDataTempSet.contains(it) }
        exposureDataSet.retainAll(exposureDataTempSet)
        removedItems.forEach { exposureCallback(it, type, false) }
        //清空临时set，防止内存泄漏
        exposureDataTempSet.clear()
    }

    /**
     * 触发回调，并赋值开始/结束曝光时间
     */
    private fun exposureCallback(
        itemData: ExposureItemData,
        type: ExposureTypeEnum,
        visible: Boolean
    ) {
        if (visible) {
            itemData.startTime = System.currentTimeMillis()
            itemData.startType = type
        } else {
            itemData.endTime = System.currentTimeMillis()
            itemData.endType = type
        }
        callback.invoke(itemData, visible)
    }

    /**
     * 可见尺寸
     * LinearLayoutManager比较对应方向上的边长，其他LayoutManager比较面积
     */
    private fun getChildVisibleArea(rv: RecyclerView): Int {
        return if (rv.layoutManager is LinearLayoutManager) {
            if ((rv.layoutManager as? LinearLayoutManager)?.orientation == LinearLayoutManager.HORIZONTAL) {
                rect.width()
            } else {
                rect.height()
            }
        } else {
            rect.width() * rect.height()
        }
    }

    /**
     * 总尺寸
     * LinearLayoutManager比较对应方向上的边长，其他LayoutManager比较面积
     */
    private fun getChildTotalArea(rv: RecyclerView, child: View): Int {
        return if (rv.layoutManager is LinearLayoutManager) {
            if ((rv.layoutManager as? LinearLayoutManager)?.orientation == LinearLayoutManager.HORIZONTAL) {
                child.width
            } else {
                child.height
            }
        } else {
            child.width * child.height
        }
    }

    /**
     * 用来装曝光条目内容的结构
     */
    data class ExposureItemData(
        /**
         * 曝光源数据
         */
        val exposureData: Any,
        /**
         * 在rv中的位置
         */
        val position: Int
    ) {

        /**
         * 开始曝光时间
         */
        var startTime: Long = 0

        /**
         * 结束曝光时间
         */
        var endTime: Long = 0

        /**
         * 曝光id，取随机uuid，用于上报时开始/结束的匹配
         */
        var exposureId: String = UUID.randomUUID().toString()

        /**
         * 曝光类型
         */
        var startType: ExposureTypeEnum = ExposureTypeEnum.UNKNOWN
        var endType: ExposureTypeEnum = ExposureTypeEnum.UNKNOWN

        /**
         * 曝光时长
         */
        fun getExposureTime(): Long {
            return endTime - startTime
        }

        override fun equals(other: Any?): Boolean {
            return other is ExposureItemData && exposureData == other.exposureData
        }

        override fun hashCode(): Int {
            return exposureData.hashCode()
        }
    }

    /**
     * 曝光类型
     */
    enum class ExposureTypeEnum(val value: String) {
        /**
         * 默认初始值
         */
        UNKNOWN("unknown"),

        /**
         * 滑动触发
         */
        SCROLL("scroll"),

        /**
         * 页面切换
         */
        COVER("cover"),

        /**
         * 列表刷新
         */
        REFRESH("refresh")

        //AdapterDataObserver似乎无法很好地区分这些操作
//        /**
//         * 条目删除
//         */
//        DELETE("delete"),
//
//        /**
//         * 条目插入
//         */
//        INSERT("insert"),
//
//        /**
//         * 条目移动
//         */
//        MOVE("move"),
    }

    companion object {
        fun bind(
            recyclerView: RecyclerView,
            @FloatRange(from = 0.0, to = 1.0, fromInclusive = false) percent: Float = 1f,
            idle: Boolean = false,
            lifecycleOwner: LifecycleOwner? = null,
            callback: (item: ExposureItemData, visible: Boolean) -> Unit
        ): RecyclerViewExposureHelper {
            return RecyclerViewExposureHelper(recyclerView, percent, idle, lifecycleOwner, callback)
        }
    }

}