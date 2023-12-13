package com.facile.immediate.electronique.fleurs.pret.home.view

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.arthur.commonlib.utils.DateUtil
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.arthur.commonlib.utils.SPUtils
import com.arthur.commonlib.utils.image.DisplayUtils
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentHomeHostBinding
import com.facile.immediate.electronique.fleurs.pret.home.Constant
import com.facile.immediate.electronique.fleurs.pret.home.vm.FirstViewModel
import com.facile.immediate.electronique.fleurs.pret.loan.model.ProState
import com.facile.immediate.electronique.fleurs.pret.loan.view.BaseLoanStateFragment
import com.facile.immediate.electronique.fleurs.pret.loan.view.EvaluationVersementFragment
import com.facile.immediate.electronique.fleurs.pret.loan.view.RejeteeFragment
import com.facile.immediate.electronique.fleurs.pret.loan.view.RemboursementRetardeFragment
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.permissionx.guolindev.PermissionX
import java.util.Date


class FirstFragment : BaseMVVMFragment<FragmentHomeHostBinding, FirstViewModel>() {

    private val multiProHFragment: Fragment by lazy {
        MultiProHFragment()
    }
    private val singleProHFragment: Fragment by lazy {
        SingleProHFragment()
    }
    private val evaluationVersementFragment: Fragment by lazy {
        EvaluationVersementFragment()
    }
    private val remboursementRetardeFragment: Fragment by lazy {
        RemboursementRetardeFragment()
    }
    private val rejeteeFragment: Fragment by lazy {
        RejeteeFragment()
    }

    override fun buildView() {
        super.buildView()
        mBinding.inTitle.clTitle.apply {
            layoutParams.height = 44f.dp2px(requireContext()) + 32f.dp2px(requireContext())
            updatePadding(top = 32f.dp2px(requireContext()))
        }
        PrivacyPolicyDisplayUtil.displayPrivacyPolicyGuide(
            requireContext(), mBinding.tvReadPrivacyPolicyGuide
        )
    }

    override fun processLogic() {
        super.processLogic()
        DisplayUtils.displayImageAsRound(
            R.mipmap.ic_launcher,
            mBinding.inTitle.ivLogo,
            R.mipmap.ic_launcher,
            radius = 6f.dp2px(requireContext())
        )
        mViewModel.getHomeData()
        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            .request { _: Boolean, _: List<String?>?, _: List<String?>? ->

            }
    }

    override fun setListener() {
        super.setListener()
        mBinding.srlRefresh.setOnRefreshListener {
            mViewModel.getHomeData()
        }
        mBinding.inTitle.ivCustomer.setOnClickListener {
            ConsumerActivity.goBranch(requireActivity())
        }
    }

    override fun onPageResume() {
        super.onPageResume()
        mBinding.srlRefresh.autoRefresh()
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.refreshCompleteLiveData.observe(viewLifecycleOwner) {
            if (mBinding.srlRefresh.isRefreshing)
                mBinding.srlRefresh.finishRefresh()
        }

        mViewModel.multiProHLiveData.observe(viewLifecycleOwner) {
            it?.let {
                mBinding.tvReadPrivacyPolicyGuide.visibility = View.VISIBLE
                commitTargetFragment(multiProHFragment)
            }
        }
        mViewModel.singleProHLiveData.observe(viewLifecycleOwner) {
            it?.let {
                mBinding.tvReadPrivacyPolicyGuide.visibility = View.GONE
                commitTargetFragment(singleProHFragment)
            }
        }

        mViewModel.ordStateLiveData.observe(viewLifecycleOwner) {
            it?.let {
                mBinding.tvReadPrivacyPolicyGuide.visibility = View.VISIBLE
                val argument = Bundle().apply {
                    putParcelable(BaseLoanStateFragment.KEY_PRO_INFO, it)
                }
                when (it.rudeReceptionCyclistArcticHunger) {
                    ProState.VERSEMENT.value.toString(),
                    ProState.VERSEMENT_ECHOUE.value.toString(),
                    ProState.EN_EVALUATION.value.toString()
                    -> {
                        commitTargetFragment(evaluationVersementFragment.apply {
                            arguments = argument
                        })
                    }

                    ProState.REMBOURSEMENT.value.toString(), ProState.RETARDE.value.toString() -> {
                        commitTargetFragment(remboursementRetardeFragment.apply {
                            arguments = argument
                        })
                    }

                    ProState.REJETEE.value.toString() -> {
                        commitTargetFragment(rejeteeFragment.apply {
                            arguments = argument
                        })
                    }

                    else -> {
                        isLoaded = false
                    }
                }
            }
            // 好评弹窗
            mViewModel.globalSetting("skillfulSkin,coolFoolishLondon,thirstyTranslatorMusicalCeilingIdea")
        }

        mViewModel.globalSettingLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.afraidDecemberSlimClassicalTechnology?.isNotEmpty() == true) {
                    mBinding.tvReadPrivacyPolicyGuide.visibility = View.GONE
                    commitTargetFragment(singleProHFragment)
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