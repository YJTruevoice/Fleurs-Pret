package com.facile.immediate.electronique.fleurs.pret.common

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.arthur.commonlib.utils.ValuesUtils
import com.facile.immediate.electronique.fleurs.pret.BuildConfig
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.view.CenterVerticalImageSpan
import com.facile.immediate.electronique.fleurs.pret.web.WebActivity

/**
 * @Author arthur
 * @Date 2023/10/18
 * @Description
 */
object PrivacyPolicyDisplayUtil {

    fun displayPrivacyPolicyGuide(context: Context, textView: TextView) {
        val ssb = SpannableStringBuilder(context.getString(R.string.text_read_privacy_policy_guide))
//        val drawable = ContextCompat.getDrawable(context, R.mipmap.icon_read_privacy_guide)
//        drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
//        val imageSpan = drawable?.let { CenterVerticalImageSpan(it) }
//        ssb.insert(0, "   ")
//        ssb.setSpan(imageSpan, 0, 1, ImageSpan.ALIGN_BASELINE)

        val blueTextStr = context.getString(R.string.text_privacy_policy)
        val blueTextStrIdx = ssb.indexOf(blueTextStr)
        ssb.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_4635FF)),
            blueTextStrIdx,
            blueTextStrIdx + blueTextStr.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    WebActivity.open(
                        context,
                        privacyLink(),
                        ValuesUtils.getString(R.string.app_name)
                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ContextCompat.getColor(context, R.color.color_4635FF)
                    ds.isUnderlineText = false
                }
            },
            blueTextStrIdx,
            blueTextStrIdx + blueTextStr.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = ssb
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    fun privacyLink(): String {
        return if (BuildConfig.MODE == 1 || BuildConfig.MODE == 2) {
            "https://test.fleurspret.com/fleursprets/privacy.html"
        } else {
            "https://www.fleurspret.com/fleursprets/privacy.html"
        }
    }
}