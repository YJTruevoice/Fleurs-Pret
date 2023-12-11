package com.facile.immediate.electronique.fleurs.pret.mine

import android.content.Intent
import androidx.core.view.updatePadding
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.arthur.commonlib.utils.ValuesUtils
import com.arthur.commonlib.utils.image.DisplayUtils
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.common.consumer.CrispMgr
import com.facile.immediate.electronique.fleurs.pret.common.event.UserInfoUpdate
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentNotificationsBinding
import com.facile.immediate.electronique.fleurs.pret.login.LogUpActivity
import com.facile.immediate.electronique.fleurs.pret.web.WebActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ThirdFragment : BaseMVVMFragment<FragmentNotificationsBinding, ThirdViewModel>() {

    override fun buildView() {
        super.buildView()
        mBinding.vBackgroundTop.apply {
            layoutParams.height = 200f.dp2px(requireContext()) + 32f.dp2px(requireContext())
            updatePadding(top = 32f.dp2px(requireContext()))
        }
    }

    override fun setListener() {
        super.setListener()
        mBinding.ivCustomer.setOnClickListener {
            ConsumerActivity.goBranch(requireContext())
        }
        mBinding.tvServiceCenter.setOnClickListener {
            ConsumerActivity.go(requireContext())
        }
        mBinding.tvServiceOnline.setOnClickListener {
            CrispMgr.launchChat(requireContext())
        }
        mBinding.tvPrivacyPolicy.setOnClickListener {
            WebActivity.open(
                requireContext(),
                PrivacyPolicyDisplayUtil.privacyLink(),
                ValuesUtils.getString(R.string.app_name)
            )
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
                mBinding.tvUserName.text =
                    if (it.dirtyCrowdedEarthquakePrivateLevel.isNullOrEmpty()) getString(R.string.app_name)
                    else it.dirtyCrowdedEarthquakePrivateLevel
                val phoneNum = UserManager.phoneNumber()
                if (phoneNum.isNotEmpty()) {
                    mBinding.tvUserPhone.text = "+221 ${phoneNum}"
                }
            }
        }
        mViewModel.userHeadImgLiveData.observe(viewLifecycleOwner) {
            DisplayUtils.displayImageAsCircle(it, mBinding.ivHeadPortrait, R.mipmap.ic_launcher)
        }
    }

    override fun onInit() {
        super.onInit()
        EventBus.getDefault().register(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(userInfoUpdate: UserInfoUpdate) {
        if (UserManager.isLogUp()) {
            mViewModel.getUserName()
            mViewModel.getUserHeadPortrait()
        }
    }
}