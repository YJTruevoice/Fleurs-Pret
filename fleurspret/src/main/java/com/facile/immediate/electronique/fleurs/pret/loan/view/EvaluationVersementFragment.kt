package com.facile.immediate.electronique.fleurs.pret.loan.view

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentLoanStateEvaluationVersementLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.loan.model.OrdStateInfo
import com.facile.immediate.electronique.fleurs.pret.loan.model.ProState

class EvaluationVersementFragment :
    BaseLoanStateFragment<FragmentLoanStateEvaluationVersementLayoutBinding>() {

    override fun privacyPolicyGuideView(): TextView {
        return mBinding.tvReadPrivacyPolicyGuide
    }

    override fun setOrdInfo(ordInfo: OrdStateInfo) {
        mBinding.apply {
            inProInfo.apply {
                tvLoanState.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                    text =
                        when (mViewModel.proInfo?.rudeReceptionCyclistArcticHunger) {
                            ProState.CAN_APPLY.value.toString() -> {
                                tvStateTips.text =
                                    getString(R.string.text_nous_sommes_en_train_d_valuer_votre_demande_du_pr_t_veuillez_attendre_patiemment_le_r_sultat_d_valuation)
                                context.getString(R.string.text_en_valuation)
                            }
                            ProState.VERSEMENT.value.toString() -> {
                                tvStateTips.text = context.getString(R.string.text_votre_demande_du_pr_t_a_t_approuv_e_le_versement_est_en_cours)
                                context.getString(R.string.text_en_train_de_versement)
                            }
                            else -> {
                                ""
                            }
                        }
                    TextViewCompat.setCompoundDrawableTintList(
                        this,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.color_4635FF
                            )
                        )
                    )
                }

                tvRepaymentGoldLabel.text = getString(R.string.text_monto_del_pr_stamo)
                tvRepaymentGold.text = ordInfo.friendlyBusinessmanHistory
                tvRepaymentDateLabel.text = getString(R.string.text_fecha_de_solicitud)
                tvRepaymentDate.text = ordInfo.tenseSpiritBallpointPoliceman
            }
        }
    }
}