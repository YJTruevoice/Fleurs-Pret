package com.facile.immediate.electronique.fleurs.pret.bottomsheet

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.ref.WeakReference

/**
 * 当maxHeight == peekHeight时，为固定大小的dialog
 */
class FixedHeightBottomSheetDialog(
        context: Context,
        theme: Int,
        private val maxHeight: Int = ViewGroup.LayoutParams.MATCH_PARENT,
        private val peekHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
        private val expend: Boolean = false,
        private val wrapHeight: Boolean = true,
        private val wrapAble: Boolean = true
) : BottomSheetDialog(context, theme) {

    //主要是防止弱引用指向的 Listener被清除
    private var mOnDismissListener: DialogInterface.OnDismissListener? = null
    private var mOnCancelListener: DialogInterface.OnCancelListener? = null
    private var mOnShowListener: DialogInterface.OnShowListener? = null

    private var currentState: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPeekHeight(peekHeight)
        setMaxHeight(maxHeight)
    }

    override fun onStart() {
        super.onStart()
        findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.let {root ->
            // 不是根据内容确定高度，指定高度
            if (!wrapHeight) {
                root.layoutParams?.height = if (isFixed()) {
                    peekHeight
                } else {
                    maxHeight
                }
            }
            root.post(Runnable {
                root.setBackgroundResource(android.R.color.transparent)
            })
        }

        getBottomSheetBehavior()?.let { behavior ->
            // 非固定高度模式下，判断是否指定展开，默认不展开
            if (!isFixed() && expend) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            // 更新当前状态
            currentState = behavior.state

            // 设置是否可滑动
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (!wrapAble && newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = currentState ?: BottomSheetBehavior.STATE_EXPANDED
                    } else {
                        currentState = newState
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }
    }

    private fun isFixed():Boolean {
        return peekHeight == maxHeight
    }

    private fun setPeekHeight(peekHeight: Int) {
        if (peekHeight <= 0) {
            return
        }
        val bottomSheetBehavior = getBottomSheetBehavior()
        bottomSheetBehavior?.peekHeight = peekHeight
    }

    private fun setMaxHeight(maxHeight: Int) {
        if (maxHeight <= 0) {
            return
        }
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight)
        window?.setGravity(Gravity.BOTTOM)
    }

    fun getBottomSheetBehavior(): BottomSheetBehavior<View>? {
        val view: View? = window?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        return view?.let { BottomSheetBehavior.from(view) }
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        mOnDismissListener = listener
        super.setOnDismissListener(
                WrappedDismissDialogListener(mOnDismissListener)
        )
    }

    override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        //包装一层，使用弱引用
        mOnShowListener = listener
        super.setOnShowListener(
                WrappedShowListener(mOnShowListener)
        )
    }

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        mOnCancelListener = listener
        super.setOnCancelListener(
                WrappedCancelListener(mOnCancelListener)
        )
    }
}

class WrappedCancelListener(delegate: DialogInterface.OnCancelListener?) :
        DialogInterface.OnCancelListener {
    private var weakRef = WeakReference(delegate)

    override fun onCancel(dialog: DialogInterface?) {
        weakRef.get()?.onCancel(dialog)
    }
}

class WrappedDismissDialogListener(delegate: DialogInterface.OnDismissListener?) :
        DialogInterface.OnDismissListener {
    private var weakRef = WeakReference(delegate)

    override fun onDismiss(dialog: DialogInterface?) {
        weakRef.get()?.onDismiss(dialog)
    }

}

class WrappedShowListener(delegate: DialogInterface.OnShowListener?) :
        DialogInterface.OnShowListener {
    private var weakRef = WeakReference(delegate)

    override fun onShow(dialog: DialogInterface?) {
        weakRef.get()?.onShow(dialog)
    }
}