package com.facile.immediate.electronique.fleurs.pret.loan.model

import com.arthur.baselib.structure.base.IBaseModel
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.common.setting.RecommendBanner
import com.facile.immediate.electronique.fleurs.pret.common.setting.SettingAPI
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr

class LoanModel : IBaseModel {
    private val settingService: SettingAPI = NetMgr.get().service()
    private val service = NetMgr.get().service<LoanAPI>()

    suspend fun globalSetting(keys:String):BaseResponse<GlobalSetting?>{
        return settingService.globalSetting(keys)
    }

    suspend fun checkOrdInfo(): BaseResponse<OrdStateInfo?>{
        return service.checkOrdInfo()
    }
    suspend fun recommendBanner(): BaseResponse<RecommendBanner?>{
        return settingService.recommendBanner()
    }
}