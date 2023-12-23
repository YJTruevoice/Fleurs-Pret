package com.arthur.baselib.structure.base.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.arthur.baselib.structure.base.IBindingView
import com.arthur.commonlib.utils.ReflectUtils

/**
 *
 * @author: guo.lei
 * @date: 2022/3/30
 */
open class BaseBindingActivity<V : ViewBinding> : BaseSimpleActivity(),
    IBindingView<V> {
    private var _binding: V? = null
    protected val mBinding get() = _binding!!

    override fun setContentView() {
        if (getLayoutResId() > 0) {
            Log.w("", "the result of getLayoutResId() is ignored in BaseBindingActivity")
        }
        initBinding(layoutInflater, null)?.let {
            _binding = it
        }
        if (_binding == null) {
            throw RuntimeException("you must use correct binding with BaseBindingActivity")
        }
        setContentView(mBinding.root)
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V? {
        val clz = ReflectUtils.getTargetTFromObj<V>(
            this,
            BaseBindingActivity::class.java,
            ViewBinding::class.java
        )
        var binding : V? = null
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}