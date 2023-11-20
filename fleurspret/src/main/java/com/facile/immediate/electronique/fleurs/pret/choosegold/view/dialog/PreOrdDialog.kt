package com.facile.immediate.electronique.fleurs.pret.choosegold.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.DialogPreOrdLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.BaseDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.CommonDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseDialog

class PreOrdDialog(
    context: Context,
    private val montantReceive: String,
    private val montantRePayment: String,
    private val dateRePayment: String
) : BaseDialog(context) {

    private lateinit var mBinding: DialogPreOrdLayoutBinding

    override fun getContentView(): View {
        mBinding = DialogPreOrdLayoutBinding.inflate(LayoutInflater.from(context))
        return mBinding.root
    }

    override fun setDialogData(config: BaseDialogConfigEntity?) {
        (config as? CommonDialogConfigEntity)?.let {
            // 公共初始化
            super.setDialogData(config)
            mBinding.tvTitle.apply {
                if (config.title?.isNotEmpty() == true) {
                    text = config.title
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }

            // 内容
            mBinding.inXofReceive.tvName.text = context.getText(R.string.text_montant_recevoir)
            mBinding.inXofReceive.tvValue.text = montantReceive

            mBinding.inXofRepayment.tvName.text =
                context.getText(R.string.text_montant_du_remboursement)
            mBinding.inXofRepayment.tvValue.text = montantRePayment

            mBinding.inXofRepaymentDate.tvName.text =
                context.getText(R.string.text_date_du_remboursement)
            mBinding.inXofRepaymentDate.tvValue.text = dateRePayment


            mBinding.tvDialogConfirm.apply {
                if (config.confirmText?.isNotEmpty() == true) {
                    text = config.confirmText
                }
                setOnClickListener {
                    config.confirmCallback?.invoke(this@PreOrdDialog)
                    dismiss()
                }
            }
            mBinding.tvDialogCancel.apply {
                if (config.cancelText?.isNotEmpty() == true) {
                    text = config.cancelText
                }
                setOnClickListener {
                    config.cancelCallback?.invoke(this@PreOrdDialog)
                    dismiss()
                }
            }
        }

    }

    companion object {
        fun with(
            context: Context,
            montantReceive: String,
            montantRePayment: String,
            dateRePayment: String
        ): Builder {
            return Builder(
                context,
                montantReceive,
                montantRePayment,
                dateRePayment
            )
        }
    }

    class Builder constructor(
        var context: Context,
        val montantReceive: String,
        val montantRePayment: String,
        val dateRePayment: String
    ) : BaseDialog.Builder<Builder>(context) {
        override fun createDialog(): BaseDialog {
            return PreOrdDialog(
                context,
                montantReceive,
                montantRePayment,
                dateRePayment
            )
        }

        override fun createConfig(): BaseDialogConfigEntity {
            return CommonDialogConfigEntity()
        }
    }
}