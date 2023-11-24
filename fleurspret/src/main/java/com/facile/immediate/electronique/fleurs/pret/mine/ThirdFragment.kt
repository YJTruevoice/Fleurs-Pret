package com.facile.immediate.electronique.fleurs.pret.mine

import android.content.Intent
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.arthur.commonlib.utils.image.DisplayUtils
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentNotificationsBinding
import com.facile.immediate.electronique.fleurs.pret.login.LogUpActivity
import com.facile.immediate.electronique.fleurs.pret.web.WebActivity

class ThirdFragment : BaseMVVMFragment<FragmentNotificationsBinding, ThirdViewModel>() {

    override fun setListener() {
        super.setListener()
        mBinding.ivCustomer.setOnClickListener {
            ConsumerActivity.go(requireContext())
        }
        mBinding.tvServiceCenter.setOnClickListener {
            ConsumerActivity.go(requireContext())
        }
        mBinding.tvServiceOnline.setOnClickListener {
            ConsumerActivity.go(requireContext())
        }
        mBinding.tvPrivacyPolicy.setOnClickListener {
            WebActivity.open(requireContext(), "https://baidu.com")
        }

        mBinding.tvQuit.setOnClickListener {
            mViewModel.quit()
            startActivity(Intent(requireContext(), LogUpActivity::class.java))
        }
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.userNameLiveData.observe(viewLifecycleOwner) {
            it?.let {
                mBinding.tvUserName.text = it.dirtyCrowdedEarthquakePrivateLevel
                mBinding.tvUserPhone.text = it.usualExtraordinaryScholarshipQuickHardship
            }
        }
        mViewModel.userHeadImgLiveData.observe(viewLifecycleOwner) {
            DisplayUtils.displayImageAsCircle(it, mBinding.ivHeadPortrait, R.mipmap.ic_launcher)
        }
    }

    override fun onLazyInit() {
        super.onLazyInit()
        mViewModel.getUserName()
        mViewModel.getUserHeadPortrait()
    }
}