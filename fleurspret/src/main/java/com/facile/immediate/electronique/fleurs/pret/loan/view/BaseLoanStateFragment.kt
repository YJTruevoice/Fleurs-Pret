package com.facile.immediate.electronique.fleurs.pret.loan.view

import android.os.Build
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.home.model.ProInfo
import com.facile.immediate.electronique.fleurs.pret.loan.model.OrdStateInfo
import com.facile.immediate.electronique.fleurs.pret.loan.vm.LoanVM

abstract class BaseLoanStateFragment<Binding : ViewBinding> : BaseMVVMFragment<Binding, LoanVM>() {
    companion object {
        const val KEY_PRO_INFO = "KEY_PRO_INFO"
    }

    override fun buildView() {
        super.buildView()
        PrivacyPolicyDisplayUtil.displayPrivacyPolicyGuide(
            requireContext(), privacyPolicyGuideView()
        )
    }

    override fun processLogic() {
        super.processLogic()
        getArgumentsBundle()?.let {
            mViewModel.proInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(KEY_PRO_INFO, ProInfo::class.java)
            } else {
                it.getParcelable(KEY_PRO_INFO)
            }
            mViewModel.checkOrdInfo()
        }
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.ordStateInfoLiveData.observe(viewLifecycleOwner) {
            it?.let {
                setOrdInfo(it)
            }
        }

        mViewModel.settingLiveData.observe(viewLifecycleOwner) {
            it?.let {
                onGlobalSetting(it)
            }
        }
    }

    abstract fun privacyPolicyGuideView(): TextView

    abstract fun setOrdInfo(ordInfo: OrdStateInfo)

    open fun onGlobalSetting(setting: GlobalSetting) {}
}