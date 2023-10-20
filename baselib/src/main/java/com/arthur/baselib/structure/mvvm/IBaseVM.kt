package com.arthur.baselib.structure.mvvm

/**
 *
 * created by guo.lei
 * on 2022.04.06
 */
interface IBaseVM<M> {
    /**
     * 创建model
     */
    fun createModel() : M

    /**
     * 初始化View
     */
    fun buildView() {}

    /**
     * 初始化数据
     */
    fun processLogic() {}

    /**
     * 初始化事件监听
     */
    fun setEvent() {}

    /**
     * 基础页面变动通知
     */
    fun initCommonUIChangeLiveData() : BaseUIChangeLiveData
}