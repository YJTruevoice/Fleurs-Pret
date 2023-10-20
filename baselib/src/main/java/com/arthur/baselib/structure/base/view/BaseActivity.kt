package com.arthur.baselib.structure.base.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.arthur.commonlib.ability.MainThreadExecutor
import com.arthur.commonlib.ability.Toaster

/**
 *
 * @author: guo.lei
 * @date: 2022/3/30
 */
abstract class BaseActivity : AppCompatActivity() {
    @JvmField
    val TAG = javaClass.simpleName

    val taskTag: Any
        get() = "${this.javaClass.name}:${hashCode()}"

    lateinit var mContext: Context

    lateinit var ac: BaseActivity

    private var dialog: Dialog? = null

    /**
     * Activity当前是否在前台
     */
    private var mIsForeground = false

    /**
     * 状态栏文字和图片是否为深色，默认深色
     */
    protected open var statusBarDarkMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        ac = this
        beforeOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        beforeSetContent()
        setContentView()
        setStatusBar()
        mContext = this
    }

    protected open fun beforeOnCreate(savedInstanceState: Bundle?) {}

    protected open fun beforeSetContent() {}

    protected open fun setContentView() {
        if (getLayoutResId() > 0) {
            setContentView(getLayoutResId())
        }
    }

    protected open fun getLayoutResId(): Int {
        return -1
    }


    protected open fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(statusBarDarkMode)
            .titleBar(getViewBelowStatusBar())
            .init()
    }

    /**
     * 状态栏下方的view，设置了沉浸式状态栏后状态栏会覆盖在view上层，需要对其进行处理
     */
    protected open fun getViewBelowStatusBar(): View? {
        return null
    }

    /**
     * 关闭当前显示的Dialog。
     */
    @Synchronized
    fun closeDialog() {
        try {
            if (dialog?.isShowing == true && !isFinishing) {
                dialog?.dismiss()
                dialog = null
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun isDialogShowing(): Boolean {
        return dialog?.isShowing == true && !isFinishing
    }

    /**
     * <pre>
     * 显示对话框。
     * 调用此方法表示将 Dialog 交予 Activity 管理。
     * BaseActivity 认为当前 Activity 仅同时存在一个 Dialog，因此调用这个方法会自动关闭之前在前台的 Dialog。
    </pre> *
     *
     * @param dialog 要显示的对话框
     */
    @Synchronized
    fun showDialog(dialog: Dialog) {
        closeDialog()
        this.dialog = dialog
        try {
            if (!isFinishing) {
                dialog.show()
            }
        } catch (e: Throwable) {
        }
    }

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
            Runnable { finish() },
            if ((delay ?: 0) <= 0) 400 else delay!!
        )
    }

    /**
     * 当前应用是否在前台
     */
    fun isForeground(): Boolean {
        return mIsForeground
    }

    override fun onPause() {
        super.onPause()
        mIsForeground = false
    }

    override fun onDestroy() {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.cancel()
            }
            dialog = null
        }
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mIsForeground = true
    }

    open fun isValid(): Boolean {
        return !isFinishing && !isDestroyed
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(attachBaseLanguageContext(newBase))

    }

    open fun attachBaseLanguageContext(context: Context?): Context? {
        return context
    }
}