package com.arthur.baselib.structure.base

interface IBaseView {
    /**
     * ui初始化
     */
    fun buildView() {}

    /**
     * 初始化数据
     */
    fun processLogic() {}

    /**
     * 初始化监听器
     */
    fun setListener() {}

    /**
     * 初始化事件监听器
     */
    fun setEvent() {}

    /**
     * 弹出toast
     */
    fun showToast(msg: CharSequence?) {}

    /**
     * View是否可用
     */
    fun isValid(): Boolean
}