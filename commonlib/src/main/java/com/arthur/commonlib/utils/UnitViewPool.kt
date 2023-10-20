package com.arthur.commonlib.utils

import android.content.Context
import android.content.MutableContextWrapper
import android.util.ArrayMap
import android.view.View
import java.util.ArrayList

object UnitViewPool {

    //默认每种view最多存五个
    const val DEFAULT_MAX_SCRAP = 5

    private val viewPoolArrayMap = ArrayMap<String, ViewScrapData>()

    internal class ViewScrapData {
        val mScrapHeap = ArrayList<View>()
        var mMaxScrap = DEFAULT_MAX_SCRAP
    }

    /**
     * 回收view
     * @param tag: view存储在viewPoolArrayMap中的key值，不传默认为view.javaClass.simpleName
     */
    fun putView(view: View, tag: String? = null) {
        val tagForPut = tag ?: view.javaClass.simpleName
        val viewScrapData = getScrapForTag(tagForPut)
        if (viewScrapData.mMaxScrap <= viewScrapData.mScrapHeap.size
            || view.parent != null
            || viewScrapData.mScrapHeap.contains(view)
        ) {
            return
        }
        (view.context as? MutableContextWrapper)?.baseContext = com.arthur.commonlib.ability.AppKit.context
        viewScrapData.mScrapHeap.add(view)
    }

    inline fun <reified T : View> getNotNullView(context: Context): T {
        val clazz = T::class.java
        val cachedView = (getView(clazz.simpleName) as? T)
        return if (cachedView == null || cachedView.context !is MutableContextWrapper) {
            clazz.getConstructor(Context::class.java)
                .newInstance(MutableContextWrapper(com.arthur.commonlib.ability.AppKit.context)).apply {
                    (getContext() as? MutableContextWrapper)?.baseContext = context
                }
        } else {
            cachedView.apply {
                (getContext() as? MutableContextWrapper)?.baseContext = context
            }
        }
    }

    fun getView(tag: String): View? {
        val viewScrapData = viewPoolArrayMap[tag]
        if (viewScrapData != null && viewScrapData.mScrapHeap.isNotEmpty()) {
            for (index in viewScrapData.mScrapHeap.size - 1 downTo 0) {
                val view = viewScrapData.mScrapHeap.removeAt(index)
                if (view.parent == null) {
                    return view
                }
            }
        }
        return null
    }

    fun clear() {
        for (item in viewPoolArrayMap) {
            item.value.mScrapHeap.clear()
        }
    }

    fun clear(tag: String) {
        viewPoolArrayMap[tag]?.mScrapHeap?.clear()
    }

    fun getTotalSize(): Int {
        var totalSize = 0
        for (item in viewPoolArrayMap) {
            totalSize += item.value.mScrapHeap.size
        }
        return totalSize
    }

    fun getRecycledViewSize(tag: String): Int {
        return viewPoolArrayMap[tag]?.mScrapHeap?.size ?: 0
    }

    fun setMaxSizeForScrap(view: View, size: Int) {
        setMaxSizeForScrap(view.javaClass.simpleName, size)
    }

    fun setMaxSizeForScrap(tag: String, size: Int) {
        val scrapData = getScrapForTag(tag)
        scrapData.mMaxScrap = size
        val scrapHeap = scrapData.mScrapHeap
        while (scrapHeap.size > size) {
            scrapHeap.removeAt(scrapHeap.size - 1)
        }
    }

    private fun getScrapForTag(tag: String): ViewScrapData {
        var viewScrapData = viewPoolArrayMap[tag]
        if (viewScrapData == null) {
            viewScrapData = ViewScrapData()
            viewPoolArrayMap[tag] = viewScrapData
        }
        return viewScrapData
    }
}