package com.immomo.moremo.base.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.arthur.baselib.structure.base.IArgumentsGetter
import com.arthur.baselib.structure.base.IBindingView
import com.arthur.baselib.structure.base.IBaseModel
import com.arthur.baselib.structure.mvvm.BaseViewModel

/**
 * V 层，这里的视图都是 Activity 或 Fragment
 */
interface IMVVMView<V : ViewBinding, VM : BaseViewModel<out IBaseModel>> : IBindingView<V>,
    IArgumentsGetter {

    /**
     * 初始化界面观察者
     */
    fun initLiveDataObserver() {}

    /**
     * 初始化通用的 UI 改变事件，基类应该在初始化后设为 final
     */
    fun initUiChangeLiveData() {}

    /**
     * 初始化视图和 VM
     */
    fun createViewModel() : VM

    /**
     * 移除事件总线监听，避免内存泄露
     */
    fun removeLiveDataBus(owner: LifecycleOwner)
}