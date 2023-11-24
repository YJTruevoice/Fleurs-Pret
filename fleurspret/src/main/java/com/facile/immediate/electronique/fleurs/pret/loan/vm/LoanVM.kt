package com.facile.immediate.electronique.fleurs.pret.loan.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.common.setting.RecommendBanner
import com.facile.immediate.electronique.fleurs.pret.home.model.ProInfo
import com.facile.immediate.electronique.fleurs.pret.loan.model.LoanModel
import com.facile.immediate.electronique.fleurs.pret.loan.model.OrdStateInfo

class LoanVM(app: Application) : BaseViewModel<LoanModel>(app) {

    val settingLiveData: SingleLiveEvent<GlobalSetting?> = SingleLiveEvent()
    val ordStateInfoLiveData: SingleLiveEvent<OrdStateInfo?> = SingleLiveEvent()
    val RecommendBannerLiveData: SingleLiveEvent<RecommendBanner?> = SingleLiveEvent()

    var proInfo: ProInfo? = null

    fun globalSetting(keys:String) {
        launchNet {
            mModel.globalSetting(keys)
        }.success {
            settingLiveData.value = it.aggressiveParentMethod
        }.launch()
    }

    fun checkOrdInfo() {
        launchNet {
            mModel.checkOrdInfo()
        }.success {
            ordStateInfoLiveData.value = it.aggressiveParentMethod
        }.showLoading(true).launch()
    }

    fun recommendBanner() {
        launchNet {
            mModel.recommendBanner()
        }.success {
            RecommendBannerLiveData.value = it.aggressiveParentMethod
        }.showLoading(true).launch()
    }

}