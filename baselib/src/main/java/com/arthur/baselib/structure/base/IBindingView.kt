package com.arthur.baselib.structure.base

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 *
 * created by guo.lei
 * on 2022.04.01
 */
interface IBindingView<V> {
    /**
     * 初始化 DataBinding，基类应该在初始化后设为 final
     */
    fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V?
}