package com.facile.immediate.electronique.fleurs.pret.home.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentHomeHostBinding
import com.facile.immediate.electronique.fleurs.pret.home.vm.FirstViewModel
import com.facile.immediate.electronique.fleurs.pret.loan.model.ProState
import com.facile.immediate.electronique.fleurs.pret.loan.view.EvaluationVersementFragment
import com.facile.immediate.electronique.fleurs.pret.loan.view.RejeteeFragment

class FirstFragment : BaseMVVMFragment<FragmentHomeHostBinding, FirstViewModel>() {

    override fun onLazyInit() {
        super.onLazyInit()
        mViewModel.sOrM()
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.prodMultiTypeLiveData.observe(viewLifecycleOwner) {
            it?.let {
                commitTargetFragment(MultiProHFragment())
            }
        }
        mViewModel.prodSingleTypeLiveData.observe(viewLifecycleOwner) {
            it?.let {
                it.normalBillClinicMercifulBay?.let { normalBillClinicMercifulBay ->
                    if (normalBillClinicMercifulBay.isEmpty() || normalBillClinicMercifulBay == "-1") {
                        commitTargetFragment(SingleProHFragment())
                    } else {
                        when (it.rudeReceptionCyclistArcticHunger) {
                            ProState.CAN_APPLY.value.toString(), ProState.VERSEMENT.value.toString() -> {
                                commitTargetFragment(EvaluationVersementFragment())
                            }

                            ProState.REMBOURSEMENT.value.toString() -> {}

                            ProState.RETARDE.value.toString() -> {}

                            ProState.EN_EVALUATION.value.toString() -> {}

                            ProState.REJETEE.value.toString() -> {
                                commitTargetFragment(RejeteeFragment())
                            }

                            ProState.VERSEMENT_ECHOUE.value.toString() -> {}

                            else -> {
                                isLoaded = false
                            }
                        }
                    }
                } ?: commitTargetFragment(SingleProHFragment())
            }
        }
    }

    private fun commitTargetFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().apply {
            replace(mBinding.fcvHomeContainer.id, fragment)
            setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
        }.commitAllowingStateLoss()
    }
}