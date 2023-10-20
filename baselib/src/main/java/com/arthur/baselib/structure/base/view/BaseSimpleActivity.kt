package com.arthur.baselib.structure.base.view

import android.os.Bundle
import com.arthur.baselib.structure.base.IBaseView

/**
 *
 * @author: guo.lei
 * @date: 2022/3/30
 */
open class BaseSimpleActivity : BaseActivity(), IBaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInit(savedInstanceState)
        initInternal()
    }

    protected open fun onInit(savedInstanceState: Bundle?) {}

    /**
     * 不要直接重写该方法，如有需要使用 [onInit] 方法
     */
    protected open fun initInternal() {
        buildView()
        setListener()
        processLogic()
        setEvent()
    }
}