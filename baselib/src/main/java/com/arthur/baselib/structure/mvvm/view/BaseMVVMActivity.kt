package com.arthur.baselib.structure.mvvm.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.immomo.moremo.base.mvvm.IMVVMView
import com.immomo.moremo.base.mvvm.MVVMUtils
import com.immomo.moremo.base.mvvm.generateDeclaredViewModel
import com.arthur.baselib.structure.base.IArgumentsGetter
import com.arthur.baselib.structure.base.IBaseModel
import com.arthur.baselib.structure.base.view.BaseBindingActivity

/**
 *
 * @author: guo.lei
 * @date: 2022/3/30
 * 使用 ViewBinding 构建 MVVM
 */
abstract class BaseMVVMActivity<Binding : ViewBinding, VM : BaseViewModel<out IBaseModel>> :
    BaseBindingActivity<Binding>(), IMVVMView<Binding, VM>,
    IArgumentsGetter {

    protected val mViewModel by lazy { createViewModel() }

    /**
     * 覆盖 initInternal 方法，调整初始化方法调用顺序
     */
    override fun initInternal() {
        mViewModel.onInit()
        mViewModel.buildView()
        buildView()
        setListener()
        initUiChangeLiveData()
        initLiveDataObserver()
        mViewModel.processLogic()
        processLogic()
        setEvent()
    }

    override fun createViewModel() : VM {
        var vm : VM = generateDeclaredViewModel(this, BaseMVVMActivity::class.java, application, this)
        vm.setArgumentsIntent(getArgumentsIntent())
        vm.setArgumentsBundle(getArgumentsBundle())
        // 让 vm 可以感知 v 的生命周期
        lifecycle.addObserver(vm)
        return vm
    }

    /**
     * 通过 setArguments 传递 bundle，在这里可以获取
     */
    override fun getArgumentsBundle(): Bundle? {
        return intent.extras
    }

    /**
     * 获取Intent
     */
    override fun getArgumentsIntent(): Intent {
        return intent
    }

    override fun initUiChangeLiveData() {
        // 关闭页面
        mViewModel.mUiChangeLiveData.finishPageEvent?.observe(this, Observer { finish() })
        // 设置结果并关闭页面
        mViewModel.mUiChangeLiveData.finishPageEventWithResult?.observe(this, Observer {
            if (it != null) {
                setResult(it.first, it.second)
            }
            finish()
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
        mViewModel.mUiChangeLiveData.startActivityEvent?.observe(
            this,
            Observer { startActivity(it) })
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
        mViewModel.mUiChangeLiveData.registerForResultEvent?.observeForever {
            it?.apply {
                block.invoke(registerForActivityResult(callback))
            }
        }
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

    protected open fun startActivityByName(name: String, bundle: Bundle?) {

    }

    protected fun <T> observeLiveData(liveData: LiveData<T>, onChange: (T) -> Unit) {
        liveData.observe(this, Observer {
            onChange.invoke(it)
        })
    }

    protected fun startActivity(
        clz: Class<out Activity>?,
        map: Map<String, *>? = null,
        bundle: Bundle? = null,
        clearTask: Boolean = false
    ) {
        val intent = MVVMUtils.getIntentByMapOrBundle(this, clz, map, bundle)
        if (clearTask) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    protected fun startActivityForResult(
        launcher: ActivityResultLauncher<Intent>,
        clz: Class<out Activity>?,
        map: Map<String, *>? = null,
        bundle: Bundle? = null
    ) {
        launcher.launch(MVVMUtils.getIntentByMapOrBundle(this, clz, map, bundle))
    }

    protected fun registerForActivityResult(callback: ActivityResultCallback<ActivityResult>)
            : ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
    }
}