package com.arthur.baselib.structure.mvvm.view

import androidx.databinding.ViewDataBinding
import com.arthur.baselib.structure.base.IBaseModel
import com.arthur.baselib.structure.mvvm.BaseViewModel

/**
 *
 * @author: guo.lei
 * @date: 2022/3/30
 */
abstract class BaseMVVMDataBindingActivity<V : ViewDataBinding, VM : BaseViewModel<out IBaseModel>> : BaseMVVMActivity<V, VM>() {

    override fun createViewModel() : VM{
        var vm : VM = super.createViewModel()
        // 让 LiveData 和 xml 可以双向绑定
        mBinding.lifecycleOwner = this
        // 通过反射，绑定 v 和 vm
        val bindingClz = com.arthur.commonlib.utils.ReflectUtils.getTargetTFromObj<Any>(this, BaseMVVMDataBindingActivity::class.java, ViewDataBinding::class.java)
        val vmClz = com.arthur.commonlib.utils.ReflectUtils.getTargetTFromObj<Any>(this, BaseMVVMDataBindingActivity::class.java, BaseViewModel::class.java)
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

    override fun onDestroy() {
        mBinding.unbind()
        super.onDestroy()
    }
}