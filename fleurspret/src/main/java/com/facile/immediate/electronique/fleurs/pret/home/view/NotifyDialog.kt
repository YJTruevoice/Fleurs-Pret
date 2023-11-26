package com.facile.immediate.electronique.fleurs.pret.home.view

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.os.SystemClock
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.DialogNotifyLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.BaseDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.CommonDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseDialog
import kotlinx.parcelize.Parcelize

class NotifyDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.CustomDialog
) : BaseDialog(context, themeResId) {

    private lateinit var mBinding: DialogNotifyLayoutBinding


    override fun getContentView(): View {
        mBinding = DialogNotifyLayoutBinding.inflate(LayoutInflater.from(context))
        return mBinding.root
    }

    override fun setDialogData(config: BaseDialogConfigEntity?) {
        (config as? NotifyDialogDialogConfigEntity)?.let {
            config.touchOutsideCancelAble = false
            // 公共初始化
            super.setDialogData(config)

            mBinding.ivClose.apply {
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

            mBinding.tvTitle.apply {
                text = config.title
            }

            mBinding.tvContent.apply {
                text = config.content
                movementMethod = ScrollingMovementMethod.getInstance()
            }

            mBinding.tvOk.apply {
                text = config.confirmText
                setOnClickListener {
                    dismiss()
                }
            }
        }
    }

    override fun dismiss() {
        mBinding.tvCountDown.stop()
        super.dismiss()
        config?.confirmCallback?.invoke(this@NotifyDialog)
    }

    companion object {
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    class Builder constructor(var context: Context) : BaseDialog.Builder<Builder>(context) {
        override fun createDialog(): BaseDialog {
            return NotifyDialog(context)
        }

        override fun createConfig(): BaseDialogConfigEntity {
            return NotifyDialogDialogConfigEntity()
        }

        fun countDown(count: Int): Builder {
            (config as? NotifyDialogDialogConfigEntity)?.countDown = count
            return this
        }

        fun content(content: String?): Builder {
            (config as? NotifyDialogDialogConfigEntity)?.content = content
            return this
        }
    }

    @Parcelize
    class NotifyDialogDialogConfigEntity(
        var countDown: Int = 60,
    ) : CommonDialogConfigEntity(), Parcelable

}