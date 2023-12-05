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
import com.facile.immediate.electronique.fleurs.pret.loan.model.PayDetail
import com.facile.immediate.electronique.fleurs.pret.loan.model.PayLink
import com.facile.immediate.electronique.fleurs.pret.loan.model.PayType
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse

class LoanVM(app: Application) : BaseViewModel<LoanModel>(app) {

    val settingLiveData: SingleLiveEvent<GlobalSetting?> = SingleLiveEvent()
    val ordStateInfoLiveData: SingleLiveEvent<OrdStateInfo?> = SingleLiveEvent()

    val recommendBannerLiveData: SingleLiveEvent<RecommendBanner?> = SingleLiveEvent()

    val payTypeLiveData: SingleLiveEvent<List<PayType>?> = SingleLiveEvent()
    val payLinkLiveData: SingleLiveEvent<PayLink?> = SingleLiveEvent()

    var proInfo: ProInfo? = null
    var ordStateInfo: OrdStateInfo? = null

    fun globalSetting(keys: String) {
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
            ordStateInfo = it.aggressiveParentMethod
            ordStateInfoLiveData.value = it.aggressiveParentMethod
        }.launch()
    }

    fun recommendBanner() {
        launchNet {
            mModel.recommendBanner()
        }.success {
            recommendBannerLiveData.value = it.aggressiveParentMethod
        }.showLoading(true).launch()
    }

    fun payList() {
        launchNet {
            mModel.getPayList()
        }.success {
            payTypeLiveData.value = it.aggressiveParentMethod
        }.launch()
    }

    fun payLink(
        expensiveRadioGreyPetScientificSystem: String,
        someMidnightThisSentenceEnglishQuilt: String
    ) {
        launchNet {
            mModel.getPayLink(
                normalBillClinicMercifulBay = ordStateInfo?.normalBillClinicMercifulBay.toString(),
                metalCancerVirtueRainfall = "00",
                expensiveRadioGreyPetScientificSystem = expensiveRadioGreyPetScientificSystem,
                someMidnightThisSentenceEnglishQuilt = someMidnightThisSentenceEnglishQuilt
            )
        }.success {
            payLinkLiveData.value = it.aggressiveParentMethod
        }.showLoading(true).launch()
    }

    suspend fun getPayDt(): BaseResponse<PayDetail?> {
        return mModel.getPayDt(
            normalBillClinicMercifulBay = ordStateInfo?.normalBillClinicMercifulBay.toString(),
            metalCancerVirtueRainfall = "01"
        )
    }

    suspend fun payTypes(): BaseResponse<List<PayType>?> {
        return mModel.getPayList()
    }
}