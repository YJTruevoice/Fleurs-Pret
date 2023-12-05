package com.facile.immediate.electronique.fleurs.pret.loan.view

import android.content.Intent
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.ext.addThousandSeparator
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentLoanStateEvaluationVersementLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.input.view.InputGatheringInformationActivity
import com.facile.immediate.electronique.fleurs.pret.loan.model.OrdStateInfo
import com.facile.immediate.electronique.fleurs.pret.loan.model.ProState

class EvaluationVersementFragment :
    BaseLoanStateFragment<FragmentLoanStateEvaluationVersementLayoutBinding>() {

    override fun setListener() {
        super.setListener()
        mBinding.btnMettreCompteReception.setOnClickListener {
            startActivity(Intent(context, InputGatheringInformationActivity::class.java).apply {
                putExtra("ordId", mViewModel.proInfo?.normalBillClinicMercifulBay)
            })
        }
    }

    override fun setOrdInfo(ordInfo: OrdStateInfo) {
        mBinding.apply {
            inProInfo.apply {
                tvLoanState.apply {
                    text =
                        when (mViewModel.proInfo?.rudeReceptionCyclistArcticHunger) {
                            ProState.EN_EVALUATION.value.toString() -> {
                                llVersementEchoueDesc.visibility = View.GONE
                                btnMettreCompteReception.visibility = View.GONE
                                tvStateTips.visibility = View.VISIBLE
                                tvStateTips.text =
                                    getString(R.string.text_nous_sommes_en_train_d_valuer_votre_demande_du_pr_t_veuillez_attendre_patiemment_le_r_sultat_d_valuation)

                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                                TextViewCompat.setCompoundDrawableTintList(
                                    this,
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.color_4635FF
                                        )
                                    )
                                )

                                context.getString(R.string.text_en_valuation)
                            }

                            ProState.VERSEMENT.value.toString() -> {
                                llVersementEchoueDesc.visibility = View.GONE
                                btnMettreCompteReception.visibility = View.GONE
                                tvStateTips.visibility = View.VISIBLE
                                tvStateTips.text =
                                    context.getString(R.string.text_votre_demande_du_pr_t_a_t_approuv_e_le_versement_est_en_cours)

                                setTextColor(ContextCompat.getColor(context, R.color.color_4635FF))
                                TextViewCompat.setCompoundDrawableTintList(
                                    this,
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(context, R.color.color_4635FF)
                                    )
                                )

                                context.getString(R.string.text_en_train_de_versement)
                            }

                            ProState.VERSEMENT_ECHOUE.value.toString() -> {
                                tvStateTips.visibility = View.GONE
                                llVersementEchoueDesc.visibility = View.VISIBLE
                                btnMettreCompteReception.visibility = View.VISIBLE

                                setTextColor(ContextCompat.getColor(context, R.color.color_FF0000))
                                TextViewCompat.setCompoundDrawableTintList(
                                    this,
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(context, R.color.color_FF0000)
                                    )
                                )

                                context.getString(R.string.text_versement_chou)
                            }

                            else -> {
                                ""
                            }
                        }
                }

                tvRepaymentGoldLabel.text = getString(R.string.text_monto_del_pr_stamo)
                tvRepaymentGold.text = ordInfo.friendlyBusinessmanHistory.addThousandSeparator()
                tvRepaymentDateLabel.text = getString(R.string.text_fecha_de_solicitud)
                tvRepaymentDate.text = ordInfo.tenseSpiritBallpointPoliceman
            }
        }
    }
}