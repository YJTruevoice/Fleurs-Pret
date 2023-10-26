package com.arthur.baselib.structure.mvvm.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.immomo.moremo.base.mvvm.IMVVMView
import com.arthur.baselib.structure.mvvm.MVVMUtils
import com.arthur.baselib.structure.mvvm.generateDeclaredViewModel
import com.arthur.baselib.structure.base.IBaseModel
import com.arthur.baselib.structure.base.view.BaseBindingFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *
 * @author: guo.lei
 * @date: 2022/3/30
 * 使用 ViewBinding 构建 MVVM
 */
abstract class BaseMVVMFragment<Binding : ViewBinding, VM : BaseViewModel<out IBaseModel>> :
    BaseBindingFragment<Binding>(), IMVVMView<Binding, VM> {

    protected val mViewModel by lazy { createViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.onInit()
    }

    /**
     * 覆盖 initInternal 方法，调整初始化方法调用顺序
     */
    override fun initInternal() {
        mViewModel.buildView()
        buildView()
        setListener()
        initUiChangeLiveData()
        initLiveDataObserver()
        mViewModel.processLogic()
        processLogic()
        setEvent()
    }

    @CallSuper
    override fun createViewModel() : VM {
        var vm : VM = generateDeclaredViewModel(
            this, BaseMVVMFragment::class.java, requireActivity().application, this
        )
        vm.setArgumentsIntent(getArgumentsIntent())
        vm.setArgumentsBundle(getArgumentsBundle())
        // 让 vm 可以感知 v 的生命周期
        lifecycle.addObserver(vm)
        return vm
    }

    override fun getArgumentsIntent(): Intent? {
        return activity?.intent
    }

    override fun getArgumentsBundle(): Bundle? {
        return arguments
    }

    override fun initUiChangeLiveData() {
        // 关闭页面
        mViewModel.mUiChangeLiveData.finishPageEvent?.observe(this, Observer { activity?.finish() })
        // 设置结果并关闭页面
        mViewModel.mUiChangeLiveData.finishPageEventWithResult?.observe(this, Observer {
            if (it != null) {
                activity?.setResult(it.first, it.second)
            }
            activity?.finish()
        })
        // 延时关闭
        mViewModel.mUiChangeLiveData.finishPageDelayEvent?.observe(this, Observer {
            it?.let {
                finishDelay(it.second, it.first)
            }
        })

        // 跳转页面
        mViewModel.mUiChangeLiveData.startActivityByNameEvent?.observe(this, Observer {
            it?.let {
                startActivityByName(it.first, it.second)
            }
        })

        // vm 可以启动界面
        mViewModel.mUiChangeLiveData.startActivityEvent?.observe(this, Observer {
            startActivity(it)
        })
        mViewModel.mUiChangeLiveData.startActivityClearTaskEvent?.observe(this, Observer {
            startActivity(it, clearTask = true)
        })
        mViewModel.mUiChangeLiveData.startActivityWithMapEvent?.observe(this, Observer {
            startActivity(it?.first, it?.second)
        })
        // vm 可以启动界面，并携带 Bundle，接收方可调用 getBundle 获取
        mViewModel.mUiChangeLiveData.startActivityEventWithBundle?.observe(this, Observer {
            startActivity(it?.first, bundle = it?.second)
        })

        // vm 可以启动界面
        mViewModel.mUiChangeLiveData.registerForResultEvent?.observe(this, Observer {
            it?.apply {
                block.invoke(registerForActivityResult(callback))
            }
        })
        mViewModel.mUiChangeLiveData.startActivityForResultEvent?.observe(this, Observer {
            it?.apply {
                startActivityForResult(launcher, clazz, null, null)
            }
        })
        // vm 可以启动界面，并携带 Bundle，接收方可调用 getBundle 获取
        mViewModel.mUiChangeLiveData.startActivityForResultEventWithBundle?.observe(this, Observer {
            it?.apply {
                startActivityForResult(launcher, clazz, null, bundle)
            }
        })
        mViewModel.mUiChangeLiveData.startActivityForResultEventWithMap?.observe(this, Observer {
            it?.apply {
                startActivityForResult(launcher, clazz, map, null)
            }
        })
    }

    override fun removeLiveDataBus(owner: LifecycleOwner) {
    }

    fun startActivity(
        clz: Class<out Activity>?,
        map: Map<String, *>? = null,
        bundle: Bundle? = null,
        clearTask: Boolean = false
    ) {
        val intent = MVVMUtils.getIntentByMapOrBundle(activity, clz, map, bundle)
        if (clearTask) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    fun startActivityForResult(
        launcher: ActivityResultLauncher<Intent>,
        clz: Class<out Activity>?,
        map: Map<String, *>? = null,
        bundle: Bundle? = null
    ) {
        launcher.launch(MVVMUtils.getIntentByMapOrBundle(activity, clz, map, bundle))
    }

    fun registerForActivityResult(callback: ActivityResultCallback<ActivityResult>)
            : ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
    }

    protected open fun startActivityByName(name: String, bundle: Bundle?) {
    }

    protected fun <T> observeLiveData(liveData: LiveData<T>, onChange: (T) -> Unit) {
        liveData.observe(this, Observer {
            onChange.invoke(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 通过反射，解决内存泄露问题
        GlobalScope.launch {
            var clz: Class<*>? = this@BaseMVVMFragment.javaClass
            while (clz != null) {
                // 找到 mBinding 所在的类
                if (clz == BaseMVVMFragment::class.java) {
                    try {
                        val field = clz.getDeclaredField("mBinding")
                        field.isAccessible = true
                        field.set(this@BaseMVVMFragment, null)
                    } catch (ignore: Exception) {
                    }
                }
                clz = clz.superclass
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // 界面销毁时移除 vm 的生命周期感知
        lifecycle.removeObserver(mViewModel)
        removeLiveDataBus(this)
    }
}