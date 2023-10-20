package com.arthur.baselib.structure.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arthur.baselib.structure.base.IBaseView

/**
 *
 * created by guo.lei on 2022.08.01
 *
 */
open class BaseSimpleFragment : BaseFragment() , IBaseView {
    @JvmField
    protected var mRootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onInit()
        if (getLayoutResourceId() > 0) {
            mRootView = inflater.inflate(getLayoutResourceId(),container,false)
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInternal()
    }

    override fun onDestroyView() {
        onRecycle()
        super.onDestroyView()
        mRootView = null
    }

    protected open fun getLayoutResourceId(): Int {
        return -1
    }

    protected open fun onInit() {}

    protected open fun onRecycle() {}

    /**
     * 不要直接重写该方法，如有需要使用 init 方法
     */
    protected open fun initInternal() {
        buildView()
        setListener()
        processLogic()
        setEvent()
    }
}