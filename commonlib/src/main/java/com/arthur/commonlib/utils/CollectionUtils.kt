package com.arthur.commonlib.utils

import android.util.SparseArray
import java.util.*

/**
 * Created by guolei on 2018/3/6.
 * 集合工具类
 */
object CollectionUtils {
    @JvmStatic
    fun isEmpty(list: List<*>?): Boolean {
        return list == null || list.isEmpty()
    }

    @JvmStatic
    fun isEmpty(list: Queue<*>?): Boolean {
        return list == null || list.isEmpty()
    }

    @JvmStatic
    fun isEmpty(map: Map<*, *>?): Boolean {
        return map == null || map.isEmpty()
    }

    @JvmStatic
    fun isEmpty(set: Set<*>?): Boolean {
        return set == null || set.isEmpty()
    }

    @JvmStatic
    fun isEmpty(arr: SparseArray<*>?): Boolean {
        return arr == null || arr.size() == 0
    }

    @JvmStatic
    fun isEmpty(arr: Array<Any?>?): Boolean {
        return arr == null || arr.isEmpty()
    }
}