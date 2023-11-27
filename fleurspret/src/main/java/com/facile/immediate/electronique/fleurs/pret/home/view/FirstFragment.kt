package com.facile.immediate.electronique.fleurs.pret.home.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.arthur.commonlib.utils.DateUtil
import com.arthur.commonlib.utils.SPUtils
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.UserManager
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentHomeHostBinding
import com.facile.immediate.electronique.fleurs.pret.home.Constant
import com.facile.immediate.electronique.fleurs.pret.home.vm.FirstViewModel
import com.facile.immediate.electronique.fleurs.pret.loan.model.ProState
import com.facile.immediate.electronique.fleurs.pret.loan.view.EvaluationVersementFragment
import com.facile.immediate.electronique.fleurs.pret.loan.view.RejeteeFragment
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import java.util.Date


class FirstFragment : BaseMVVMFragment<FragmentHomeHostBinding, FirstViewModel>() {

    override fun onLazyInit() {
        super.onLazyInit()
        if (UserManager.isLogUp()) {
            mViewModel.sOrM()
        } else {
            mViewModel.globalSetting("afraidDecemberSlimClassicalTechnology,brownTopic")
        }
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
                        // 好评弹窗
                        mViewModel.globalSetting("skillfulSkin,coolFoolishLondon,thirstyTranslatorMusicalCeilingIdea")
                    }
                } ?: commitTargetFragment(SingleProHFragment())
            }

            // 消息公告弹窗
            mViewModel.globalSetting("peacefulPartBrain,toughHydrogenMedicalTriangleSuffering,honestDessertUnableReceiptHotIceland")
        }

        mViewModel.globalSettingLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.afraidDecemberSlimClassicalTechnology?.isNotEmpty() ==true){
                    commitTargetFragment(SingleProHFragment())
                    return@observe
                }
                val curTime = System.currentTimeMillis()
                var lastShowTime = SPUtils.getLong(Constant.KEY_NOTIFY_SHOW_TIME)

                if (it.peacefulPartBrain == "1"
                    && DateUtil.getDayDiffer(Date(lastShowTime), Date(curTime)) >= 1
                    && isResumed
                ) {
                    NotifyDialog.with(requireContext())
                        .countDown(60)
                        .title(it.toughHydrogenMedicalTriangleSuffering)
                        .content(it.honestDessertUnableReceiptHotIceland)
                        .confirm(getString(R.string.text_ok)) {
                            SPUtils.putData(
                                Constant.KEY_NOTIFY_SHOW_TIME,
                                System.currentTimeMillis()
                            )
                        }
                        .build().show()
                    return@observe
                }

                lastShowTime = SPUtils.getLong(Constant.KEY_GOOD_VIEWS_SHOW_TIME)
                if (it.thirstyTranslatorMusicalCeilingIdea == "1"
                    && it.skillfulSkin == "1"
                    && DateUtil.getDayDiffer(Date(lastShowTime), Date(curTime)) >= 7
                    && isResumed
                ) {
                    startGooglePlay()
                }
            }
        }
    }

    private fun commitTargetFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().apply {
            replace(mBinding.fcvHomeContainer.id, fragment)
            setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
        }.commitAllowingStateLoss()
    }

    private fun startGooglePlay() {
        val manager = ReviewManagerFactory.create(requireContext())
        val request: Task<ReviewInfo> = manager.requestReviewFlow()
        //平台埋点
//        UploadPoint("本包对应的google好评弹框埋点位置")
        //
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                reviewInfo.describeContents()
                activity?.let {
                    val flow: Task<Void> = manager.launchReviewFlow(it, reviewInfo)
                    flow.addOnCompleteListener {

                    }
                }
            }
        }
    }
}