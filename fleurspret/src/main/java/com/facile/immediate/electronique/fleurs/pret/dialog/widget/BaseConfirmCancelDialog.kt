package com.facile.immediate.electronique.fleurs.pret.dialog.widget

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.DialogConfirmCancelLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.BaseDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.CommonDialogConfigEntity

open class BaseConfirmCancelDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.CustomDialog
) : BaseDialog(context, themeResId) {

    private lateinit var mBinding: DialogConfirmCancelLayoutBinding


    override fun getContentView(): View {
        mBinding = DialogConfirmCancelLayoutBinding.inflate(LayoutInflater.from(context))
        return mBinding.root
    }

    override fun setDialogData(config: BaseDialogConfigEntity?) {
        (config as? CommonDialogConfigEntity)?.let {
            // 关闭按钮和是否显示图片有关
            mBinding.tvDialogCancel.apply {
                setOnClickListener {
                    dismiss()
                }
            }
            // 公共初始化
            super.setDialogData(config)

            // 内容
            mBinding.flContent.apply {
                if (contentView() == null) {
                    mBinding.tvDefaultContent.apply {
                        text = config.content
                        visibility = View.VISIBLE
                    }
                } else {
                    visibility = View.GONE
                    removeAllViews()
                    addView(contentView())
                }
            }
        }

    }

    open fun contentView(): View? {
        return null
    }

    /**
     * 调整内容部分最大高度
     * @param height px
     */
    private fun adjustContentMaxHeight(height: Int) {
        ConstraintSet().apply {
            clone(mBinding.clContent)
            constrainMaxHeight(mBinding.svContent.id, height)
        }.applyTo(mBinding.clContent)
    }

    companion object {
        fun with(context: Context): Builder {
            return Builder(context)
        }

        /**
         * 无图下内容最大高度 dp
         */
        private const val MAX_CONTENT_HEIGHT = 480f

        /**
         * 内容最小高度 dp
         */
        private const val MIN_CONTENT_HEIGHT = 100f

        /**
         * 图片最大高度 dp
         */
        const val MAX_IMG_HEIGHT = MAX_CONTENT_HEIGHT - MIN_CONTENT_HEIGHT
    }

    class Builder constructor(var context: Context) : BaseDialog.Builder<Builder>(context) {
        override fun createDialog(): BaseDialog {
            return BaseConfirmCancelDialog(context)
        }

        override fun createConfig(): BaseDialogConfigEntity {
            return CommonDialogConfigEntity()
        }

        /**
         * 正文
         */
        fun content(content: CharSequence?): Builder {
            content?.let {
                (config as? CommonDialogConfigEntity)?.content = it
            }
            return this
        }

        /**
         * 正文
         */
        fun content(content: String?): Builder {
            content?.let {
                (config as? CommonDialogConfigEntity)?.content = it
            }
            return this
        }

        /**
         * 富文本正文
         */
        fun richContent(content: String?): Builder {
            content?.let {
                (config as? CommonDialogConfigEntity)?.richContent = it
            }
            return this
        }

        /**
         * 描述
         */
        fun hint(hint: String?): Builder {
            hint?.let {
                (config as? CommonDialogConfigEntity)?.hint = it
            }
            return this
        }

        /**
         * 图片资源ID - 和url同时设置优先级低于url
         * @param resId
         * @param height 图片高度，px
         */
        fun image(resId: Int, height: Int = LayoutParams.WRAP_CONTENT): Builder {
            if (resId > 0) {
                (config as? CommonDialogConfigEntity)?.let {
                    it.imgResId = resId
                    it.setImageHeight(height)
                }
            }
            return this
        }

        /**
         * 图片链接 - 和resId同时设置优先级高于resId
         * @param url
         * @param height 图片高度，px
         */
        fun image(url: String?, height: Int = LayoutParams.WRAP_CONTENT): Builder {
            url?.let {
                (config as? CommonDialogConfigEntity)?.let {
                    it.imgUrl = url
                    it.setImageHeight(height)
                }
            }
            return this
        }
    }
}