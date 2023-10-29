package com.arthur.baselib.structure.base.view

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.arthur.commonlib.ability.MainThreadExecutor
import com.arthur.commonlib.ability.Toaster

/**
 * @author: guo.lei
 * @date: 2022.3.30
 */
abstract class BaseFragment : Fragment() {
    @JvmField
    val TAG = javaClass.simpleName

    val taskTag: Any
        get() = "${this.javaClass.name}:${hashCode()}"

    /**
     * 是否已经加载
     */
    var isLoaded = false

    /**
     * Fragment是否可见
     */
    @JvmField
    var isVisiable = false

    var ac: FragmentActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ac = activity
        super.onCreate(savedInstanceState)
    }

    /**
     * View视图创建，恢复暂存的数据
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let { restoreState(it) }
    }

    /**
     * 配合 MddFragmentUtil 使用，每当 fragment 可见时回调
     */
    override fun onResume() {
        super.onResume()
        isVisiable = true
        onPageResume()
        //增加了Fragment是否可见的判断
        if (!isLoaded && !isHidden) {
            onLazyInit()
            isLoaded = true
        }
    }

    /**
     * 页面不可见
     */
    override fun onPause() {
        super.onPause()
        isVisiable = false
        onPageHidden()
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
        innerSaveStateToArguments()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        innerSaveStateToArguments()
    }

    private fun innerSaveStateToArguments() {
        saveStateToArguments()?.let {
            arguments?.putBundle("internalSavedViewState8954201239547", it)
        }
    }

    /**
     * 从argument恢复数据
     */
    private fun innerRestoreStateFromArguments(): Boolean {
        return arguments?.getBundle("internalSavedViewState8954201239547")?.let {
            restoreStateFromArguments(it)
            true
        } ?: false
    }

    /**
     * 保存数据到arguments
     * onSaveInstanceState或onDestroyView
     * 页面创建时可以从argument恢复
     */
    open fun saveStateToArguments(): Bundle? {
        return null
    }

    /**
     * 从arguments恢复数据
     */
    open fun restoreStateFromArguments(argumentBundle: Bundle) {
    }

    /**
     * 保存数据到InstantState
     * onSaveInstanceState中调用
     * onViewCreated(页面创建时从savedInstanceState中恢复
     */
    open fun saveState(outState: Bundle) {}

    /**
     * 页面创建时从savedInstanceState中恢复数据
     */
    open fun restoreState(savedInstanceState: Bundle) {}

    /**
     * 延时退出
     */
    protected fun finishDelay(text: String? = null, delay: Long? = 0) {
        if (!com.arthur.commonlib.utils.StringUtil.isEmpty(text)) {
            if (text != null) {
                Toaster.showToast(text)
            }
        }
        MainThreadExecutor.postDelayed(
            TAG,
            Runnable { activity?.finish() },
            if ((delay ?: 0) <= 0) 400 else delay!!
        )
    }

    /**
     * 仅在 fragment 第一次可见时调用，需要配合 FragmentUtil 使用
     */
    open fun onLazyInit() {}

    @CallSuper
    protected open fun onPageResume() {
//        MDLog.d("BaseFragment", "onPageResume:$taskTag")
    }

    @CallSuper
    protected open fun onPageHidden() {
//        MDLog.d("BaseFragment", "onPageHidden:$taskTag")
    }

    open fun isValid(): Boolean {
        return view != null && activity?.let {
            !it.isFinishing && !it.isDestroyed
        } ?: false
    }
}