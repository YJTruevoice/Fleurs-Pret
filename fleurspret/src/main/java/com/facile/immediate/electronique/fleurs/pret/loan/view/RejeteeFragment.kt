package com.facile.immediate.electronique.fleurs.pret.loan.view

import android.content.res.ColorStateList
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.arthur.commonlib.utils.ValuesUtils
import com.arthur.commonlib.utils.image.DisplayUtils
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentLoanStateRejeteeLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.loan.model.OrdStateInfo
import com.facile.immediate.electronique.fleurs.pret.loan.view.dialog.RecommendProDialog
import com.facile.immediate.electronique.fleurs.pret.web.WebActivity

class RejeteeFragment :
    BaseLoanStateFragment<FragmentLoanStateRejeteeLayoutBinding>() {

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.RecommendBannerLiveData.observe(viewLifecycleOwner) {
            it?.let { recommendBanner ->
                mBinding.ivBanner.apply {
                    DisplayUtils.displayImageAsRound(
                        recommendBanner.unableDam,
                        this,
                        radius = 6f.dp2px(context)
                    )
                    setOnClickListener {
                        if (recommendBanner.deepInterpreterPoliteFreshElectricity.isNullOrEmpty()) {
                            WebActivity.open(context, recommendBanner.unableDam ?: "")
                        } else {
                            RecommendProDialog.with(context)
                                .countDown(60)
                                .recommendPros(recommendBanner.deepInterpreterPoliteFreshElectricity)
                                .build().show()
                        }
                    }
                }
            }
        }
    }

    override fun privacyPolicyGuideView(): TextView {
        return mBinding.tvReadPrivacyPolicyGuide
    }

    override fun setOrdInfo(ordInfo: OrdStateInfo) {
        mViewModel.globalSetting("australianPublicChallenge,thoseLongBaseball,swiftInnExcellentContribution")
        mBinding.apply {
            inProInfo.apply {
                tvLoanState.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.color_FF0000))
                    text = context.getString(R.string.text_la_demande_a_t_rejet_e)
                    TextViewCompat.setCompoundDrawableTintList(
                        this,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.color_FF0000
                            )
                        )
                    )
                }
            }
        }
    }

    override fun onGlobalSetting(setting: GlobalSetting) {

        val thoseLongBaseball = setting.thoseLongBaseball ?: ""
        val str = String.format(
            ValuesUtils.getString(R.string.text_rejetee_desc),
            thoseLongBaseball
        )
        val ssb = SpannableStringBuilder(str)

        val blueTextStartIdx = ssb.indexOf(thoseLongBaseball)
        ssb.setSpan(
            ForegroundColorSpan(ValuesUtils.getColor(R.color.color_4635FF)),
            blueTextStartIdx, blueTextStartIdx + thoseLongBaseball.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        mBinding.tvStateTips.text = ValuesUtils.getString(R.string.text_rejetee_desc)
    }
}