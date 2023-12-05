package com.facile.immediate.electronique.fleurs.pret.loan.view

import android.os.Build
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.home.model.ProInfo
import com.facile.immediate.electronique.fleurs.pret.home.vm.FirstViewModel
import com.facile.immediate.electronique.fleurs.pret.loan.model.OrdStateInfo
import com.facile.immediate.electronique.fleurs.pret.loan.vm.LoanVM

abstract class BaseLoanStateFragment<Binding : ViewBinding> : BaseMVVMFragment<Binding, LoanVM>() {

    private val homeVM: FirstViewModel by viewModels(ownerProducer = { requireParentFragment() })

    companion object {
        const val KEY_PRO_INFO = "KEY_PRO_INFO"
    }

    override fun processLogic() {
        super.processLogic()
        getData()
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        homeVM.ordStateLiveData.observe(viewLifecycleOwner) {
            getData()
        }
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

    private fun getData() {
        getArgumentsBundle()?.let {
            mViewModel.proInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(KEY_PRO_INFO, ProInfo::class.java)
            } else {
                it.getParcelable(KEY_PRO_INFO)
            }
            mViewModel.checkOrdInfo()
        }
    }

    abstract fun setOrdInfo(ordInfo: OrdStateInfo)

    open fun onGlobalSetting(setting: GlobalSetting) {}
}