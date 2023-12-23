package com.arthur.baselib.structure.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.arthur.baselib.structure.base.IBindingView
import com.arthur.commonlib.utils.ReflectUtils

/**
 *
 * @author: guo.lei
 * @date: 2022.04.01
 *
 * 声明了基础结构的 Fragment，一些简单的 Fragment 可以直接继承使用
 */
open class BaseBindingFragment<V : ViewBinding> : BaseSimpleFragment(), IBindingView<V> {
    private var _binding: V? = null
    protected val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onInit()
        initBinding(layoutInflater, null)?.let {
            _binding = it
        }

        if (_binding == null) {
            throw RuntimeException("you must use correct binding with BaseBindingFragment")
        }
        mRootView = mBinding.root
        return mRootView
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V? {
        val clz = ReflectUtils.getTargetTFromObj<V>(
            this,
            BaseBindingFragment::class.java,
            ViewBinding::class.java
        )
        var binding: V? = null
        if (clz != null) {
            for (method in clz.methods) {
                if ("inflate" == method.name && method.parameterTypes.size == 1) {
                    binding = method.invoke(clz, inflater) as V
                    break
                }
            }
        }
        return binding
    }

    override fun isValid(): Boolean {
        return _binding != null && super.isValid()
    }

    override fun onDestroyView() {
        onRecycle()
        super.onDestroyView()
        _binding = null
    }
}