package com.arthur.commonlib.ability

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast

/**
 * 全局Toast管理
 * 以tag（种类）为维度组织Toast，不同tag的Toast有且只有一个
 * 无论什么类型的Toast，同一时间只能展示一个
 */
object Toaster {

    /**
     * 默认类型
     */
    const val DEFAULT_TAG = "default"

    private val TAG = "Toaster"

    private var mApplication: Context? = null

    /**
     * 当前正在展示的Toast，仅一个
     */
    private var mCurrShowingToast: Toast? = null

    private val mToasterMap: MutableMap<String, IToaster> by lazy { mutableMapOf() }

    fun init(context: Context): Toaster {
        mApplication = context.applicationContext
        return this
    }

    fun register(tag: String = DEFAULT_TAG, toast: IToaster?): Toaster {
        toast?.let {
            mToasterMap[tag] = toast
        }
        return this
    }

    @JvmOverloads
    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT, tag: String = DEFAULT_TAG) {
        showToastWithLocation(
            message,
            duration,
            mToasterMap[tag]
        )
    }

    private fun showToastWithLocation(
        message: String,
        duration: Int = Toast.LENGTH_SHORT,
        toaster: IToaster?
    ) {
        if (mApplication == null) {
            Log.e(TAG, "尚未初始化，需要调用init()方法")
            return
        }
        MainThreadExecutor.post {
            cancelToast()
            mCurrShowingToast = Toast.makeText(mApplication, "", duration)
            toaster?.let {
                val toastView = toaster.getToastView(message)
                if (toastView == null) {
                    mCurrShowingToast?.setText(message)
                } else {
                    mCurrShowingToast?.view = toastView
                }
                toaster.getLocationInfo().let { locationInfo ->
                    mCurrShowingToast?.setGravity(
                        locationInfo.gravity,
                        locationInfo.xOffset,
                        locationInfo.yOffset
                    )
                }
                toaster.onToast(message)
            } ?: kotlin.run {
                mCurrShowingToast?.setText(message)
            }

            mCurrShowingToast?.show()
        }
    }

    fun cancelToast() {
        mCurrShowingToast?.cancel()
    }

}

interface IToaster {
    /**
     * 自定义toast视图
     */
    fun getToastView(message: String): View?

    /**
     * 发送toast时回调
     */
    fun onToast(message: String)

    /**
     * toast位置信息
     */
    fun getLocationInfo(): ToastLocationInfo
}

data class ToastLocationInfo(
    /**
     * Gravity.getGravity
     */
    val gravity: Int = Gravity.CENTER,
    val xOffset: Int = 0,
    val yOffset: Int = 0
)