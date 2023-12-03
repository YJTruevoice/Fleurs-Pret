package com.facile.immediate.electronique.fleurs.pret.loan.model

import com.arthur.baselib.structure.base.IBaseModel
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.common.setting.RecommendBanner
import com.facile.immediate.electronique.fleurs.pret.common.setting.SettingAPI
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import retrofit2.http.Field

class LoanModel : IBaseModel {
    private val settingService: SettingAPI = NetMgr.get().service()
    private val service = NetMgr.get().service<LoanAPI>()

    suspend fun globalSetting(keys: String): BaseResponse<GlobalSetting?> {
        return settingService.globalSetting(keys)
    }

    suspend fun recommendBanner(): BaseResponse<RecommendBanner?> {
        return settingService.recommendBanner()
    }

    suspend fun checkOrdInfo(): BaseResponse<OrdStateInfo?> {
        return service.checkOrdInfo()
    }

    suspend fun getPayList(): BaseResponse<List<PayType>?> {
        return service.getPayList()
    }

    suspend fun getPayLink(
        normalBillClinicMercifulBay: String,
        metalCancerVirtueRainfall: String,
        expensiveRadioGreyPetScientificSystem: String,
        someMidnightThisSentenceEnglishQuilt: String,
    ): BaseResponse<PayLink?> {
        return service.getPayLink(
            normalBillClinicMercifulBay,
            metalCancerVirtueRainfall,
            expensiveRadioGreyPetScientificSystem,
            someMidnightThisSentenceEnglishQuilt
        )
    }
    suspend fun getPayDt(
        normalBillClinicMercifulBay: String,
        metalCancerVirtueRainfall: String
    ): BaseResponse<PayDetail?> {
        return service.getPayDt(
            normalBillClinicMercifulBay,
            metalCancerVirtueRainfall
        )
    }
}