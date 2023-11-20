package com.facile.immediate.electronique.fleurs.pret.choosegold.view.dialog

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.DialogLockedRepaymentTipBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.BaseDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.CommonDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseDialog
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseOnlyContentDialog

class LockedRepaymentDateTip(context: Context) : BaseOnlyContentDialog(context) {

    private val binding: DialogLockedRepaymentTipBinding

    init {
        binding = DialogLockedRepaymentTipBinding.inflate(LayoutInflater.from(context))
    }

    override fun contentView(): View {
        val ssb =
            SpannableStringBuilder(context.getString(R.string.text_montant_du_pr_t_augment_de_50_100))

        val redTextStart = "50"
        val redTextStartIdx = ssb.indexOf(redTextStart)
        ssb.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_FF0000)),
            redTextStartIdx, ssb.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvMontantGuide.text = ssb

        return binding.root
    }


    companion object {
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    class Builder constructor(var context: Context) : BaseDialog.Builder<Builder>(context) {
        override fun createDialog(): BaseDialog {
            return LockedRepaymentDateTip(context)
        }

        override fun createConfig(): BaseDialogConfigEntity {
            return CommonDialogConfigEntity()
        }
    }
}