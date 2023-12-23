package com.arthur.baselib.structure.mvvm.view

import androidx.databinding.ViewDataBinding
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.base.IBaseModel
import com.arthur.commonlib.utils.ReflectUtils

/**
 *
 * @author: guo.lei
 * @date: 2022.03.31
 *
 * 使用 DataBinding 的 MVVM 架构 Fragment
 */
abstract class BaseMVVMDataBindingFragment<V : ViewDataBinding, VM : BaseViewModel<out IBaseModel>> : BaseMVVMFragment<V, VM>() {

    final override fun createViewModel() : VM {
        var vm : VM = super.createViewModel()
        // 让 LiveData 和 xml 可以双向绑定
        mBinding.lifecycleOwner = viewLifecycleOwner
        // 通过反射，绑定 v 和 vm
        val bindingClz = ReflectUtils.getTargetTFromObj<Any>(this, BaseMVVMDataBindingFragment::class.java, ViewDataBinding::class.java)
        val vmClz = ReflectUtils.getTargetTFromObj<Any>(this, BaseMVVMDataBindingFragment::class.java, BaseViewModel::class.java)
        if (bindingClz != null && vmClz != null) {
            val fields = bindingClz.declaredFields
            for (field in fields) {
                if (vmClz.isAssignableFrom(field.type)) {
                    field.isAccessible = true
                    field.set(mBinding, vm)
                }
            }
        } else {
//            MDLog.e(javaClass.simpleName, "---------请检查是否声明了 ViewDataBinding 泛型参数---------")
        }
        return vm
    }

    override fun onDestroyView() {
        mBinding.unbind()
        super.onDestroyView()
    }
}