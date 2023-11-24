package com.facile.immediate.electronique.fleurs.pret.dialog.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.DialogCountDownLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.BaseDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.CommonDialogConfigEntity
import kotlinx.parcelize.Parcelize

open class BaseCountDownDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.CustomDialog
) : BaseDialog(context, themeResId) {

    private lateinit var mBinding: DialogCountDownLayoutBinding


    override fun getContentView(): View {
        mBinding = DialogCountDownLayoutBinding.inflate(LayoutInflater.from(context))
        return mBinding.root
    }

    override fun setDialogData(config: BaseDialogConfigEntity?) {
        (config as? CountDownDialogConfigEntity)?.let {
            // 公共初始化
            super.setDialogData(config)

            // 图
            mBinding.ivImg.apply {
                if (config.imgResId > 0) {
                    setImageResource(config.imgResId)
                }
            }
            // content
            mBinding.tvContent.apply {
                if (config.content?.isNotEmpty() == true) {
                    text = config.content
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }
            // ok
            mBinding.tvOk.apply {
                text = if (config.confirmText?.isNotEmpty() == true) {
                    config.confirmText
                } else {
                    context.resources.getString(R.string.text_ok)
                }
                setOnClickListener {
                    dismiss()
                }
            }

            // count down
            mBinding.tvCountDown.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    isCountDown = true
                }
                base = SystemClock.elapsedRealtime() + (config.countDown + 1) * 1000
                setOnChronometerTickListener {
                    val curElapsedRealtime = SystemClock.elapsedRealtime()
                    if (base <= curElapsedRealtime) {
                        stop()
                        dismiss()
                    } else {
                        it.text = String.format("%sS", (base - curElapsedRealtime) / 1000)
                    }
                }
            }.start()
        }
    }

    override fun dismiss() {
        mBinding.tvCountDown.stop()
        super.dismiss()
        config?.confirmCallback?.invoke(this@BaseCountDownDialog)
    }

    companion object {
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    class Builder constructor(var context: Context) : BaseDialog.Builder<Builder>(context) {
        override fun createDialog(): BaseDialog {
            return BaseCountDownDialog(context)
        }

        override fun createConfig(): BaseDialogConfigEntity {
            return CountDownDialogConfigEntity()
        }

        fun img(imgResId: Int?): Builder {
            imgResId?.let {
                (config as? CountDownDialogConfigEntity)?.imgResId = it
            }
            return this
        }

        fun content(content: String?): Builder {
            content?.let {
                (config as? CountDownDialogConfigEntity)?.content = it
            }
            return this
        }

        fun countDown(count: Int?): Builder {
            count?.let {
                (config as? CountDownDialogConfigEntity)?.countDown = it
            }
            return this
        }
    }

    @Parcelize
    class CountDownDialogConfigEntity(
        var countDown: Int = 3
    ) : CommonDialogConfigEntity(), Parcelable

}