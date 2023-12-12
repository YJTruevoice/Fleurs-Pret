package com.facile.immediate.electronique.fleurs.pret.bottomsheet

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.facile.immediate.electronique.fleurs.pret.BuildConfig
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentBottomsheetRootBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog : BottomSheetDialogFragment(), IDialogPerformance {
    private var _binding: FragmentBottomsheetRootBinding? = null
    private val mBinding get() = _binding!!

    /**
     * 最大高度
     */
    private var maxHeight: Int = 0

    /**
     * 默认高度
     */
    private var peekHeight: Int = 0

    /**
     * 是否展开，非固定高度模式有效
     */
    private var expend: Boolean = false

    /**
     * 由内容确定最小高度
     */
    private var wrapHeight: Boolean = false

    /**
     * 内容承载Fragment
     */
    private var content: IDialogContent? = null

    /**
     * 返回键监听
     */
    private var onBackCb: (() -> Unit)? = null

    /**
     * 点击外部消失
     */
    private var cancelOnTouchOutside: Boolean = true

    /**
     * 是否可滑动
     */
    private var wrapAble: Boolean = true


    private var onShowListener: OnShowListener? = null
    private var onDismissListener: OnDismissListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomsheetRootBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (content == null) {
            return
        }
        when (content) {
            is Fragment -> {
                childFragmentManager.beginTransaction()
                    .replace(R.id.fl_root, content as Fragment)
                    .commit()
            }

            else -> {
                if (BuildConfig.DEBUG) {
                    throw RuntimeException("dialog content only fragment is supported now.")
                }
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (manager.isStateSaved) {
            return
        }
        super.show(manager, tag)
        onShowListener?.onShow(dialog)
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        if (manager.isStateSaved) {
            return
        }
        manager.beginTransaction().remove(this).commitNowAllowingStateLoss()
        super.showNow(manager, tag)
    }

    //重点是这个方法，重写并返回FixedHeightBottomSheetDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return FixedHeightBottomSheetDialog(
            requireContext(),
            theme,
            maxHeight,
            peekHeight,
            expend,
            wrapHeight,
            wrapAble
        )
    }

    override fun onResume() {
        super.onResume()
        dialog?.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && onBackCb != null) {
                onBackCb!!.invoke()
                true
            } else false
        }
        dialog?.setCanceledOnTouchOutside(cancelOnTouchOutside)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss(dialog)
    }

    companion object {
        fun withFixedHeight(): FixHeightBuilder {
            return FixHeightBuilder()
        }

        fun withCollapsibleHeight(): CollapsibleBuilder {
            return CollapsibleBuilder()
        }
    }

    /**
     * 固定高度构造器
     */
    class FixHeightBuilder : BaseBuilder<FixHeightBuilder>() {
        private var height: Int = BottomSheetConstants.BOTTOM_SHEET_HEIGHT_DEFAULT
        fun height(height: Int): FixHeightBuilder {
            if (height > 0) {
                this.height = height
            }
            return this
        }

        override fun build(): BottomSheetDialog {
            return create().apply {
                this.maxHeight = height
                this.peekHeight = height
                this.onShowListener = this@FixHeightBuilder.onShowListener
                this.onDismissListener = this@FixHeightBuilder.onDismissListener
            }
        }
    }

    /**
     * 二段高度构造器
     */
    class CollapsibleBuilder : BaseBuilder<CollapsibleBuilder>() {
        private var expend: Boolean = false
        private var maxHeight: Int = BottomSheetConstants.BOTTOM_SHEET_HEIGHT_DEFAULT
        private var peekHeight: Int = (maxHeight * 0.75).toInt()

        fun maxHeight(height: Int): CollapsibleBuilder {
            if (height > 0) {
                this.maxHeight = height
            }
            return this
        }

        fun peekHeight(height: Int): CollapsibleBuilder {
            if (height > 0) {
                this.peekHeight = height
            }
            return this
        }

        fun expend(expend: Boolean): CollapsibleBuilder {
            this.expend = expend
            return this
        }

        override fun build(): BottomSheetDialog {
            return create().apply {
                this.maxHeight = this@CollapsibleBuilder.maxHeight
                this.peekHeight = this@CollapsibleBuilder.peekHeight
                this.expend = this@CollapsibleBuilder.expend
            }
        }
    }

    /**
     * 基础构建器
     */
    abstract class BaseBuilder<B : BaseBuilder<B>> {
        /**
         * 具体承载内容展示的Fragment
         */
        protected var contentFragment: IDialogContent? = null

        /**
         * 没有超出高度时，是否由内容确定高度
         */
        protected var wrapHeight: Boolean = true

        /**
         * 返回拦截，设置后禁止默认关闭行为
         */
        protected var onBackCb: (() -> Unit)? = null

        /**
         * 点击外侧是否消失
         */
        protected var cancelOnTouchOutside: Boolean = true

        /**
         * 是否可滑动
         */
        protected var canWrap: Boolean = true

        protected var onShowListener: OnShowListener? = null
        protected var onDismissListener: OnDismissListener? = null

        /**
         * 内容没有超出高度时，是否由内容确定高度
         */
        fun wrapHeight(wrapHeight: Boolean): B {
            this.wrapHeight = wrapHeight
            return this as B
        }

        fun content(fragment: IDialogContent?): B {
            this.contentFragment = fragment
            return this as B
        }

        fun onBackPressCb(onBackPressCb: (() -> Unit)?): B {
            this.onBackCb = onBackPressCb
            return this as B
        }

        fun cancelOnTouchOutsize(cancelOnTouchOutside: Boolean): B {
            this.cancelOnTouchOutside = cancelOnTouchOutside
            return this as B
        }

        fun canWrap(canWrap: Boolean): B {
            this.canWrap = canWrap
            return this as B
        }

        fun onShowListener(onShowListener: OnShowListener?): B {
            this.onShowListener = onShowListener
            return this as B
        }

        fun onDismissListener(onDismissListener: OnDismissListener?): B {
            this.onDismissListener = onDismissListener
            return this as B
        }

        protected fun create(): BottomSheetDialog {
            return BottomSheetDialog().apply {
                this.onBackCb = this@BaseBuilder.onBackCb
                this.cancelOnTouchOutside = this@BaseBuilder.cancelOnTouchOutside
                this.wrapAble = this@BaseBuilder.canWrap
                this.content = this@BaseBuilder.contentFragment
                this.wrapHeight = this@BaseBuilder.wrapHeight
            }
        }

        abstract fun build(): BottomSheetDialog
    }
}