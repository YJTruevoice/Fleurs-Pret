package com.facile.immediate.electronique.fleurs.pret.home.model

import com.arthur.baselib.structure.base.IBaseModel
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.common.setting.SettingAPI
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr

class FirstModel : IBaseModel {
    private val settingService: SettingAPI = NetMgr.get().service()
    private val service: FirstAPI = NetMgr.get().service()

    suspend fun multiOrSingle(): BaseResponse<List<ProInfo>?> {
        return service.multiOrSingle()
    }

    suspend fun globalSetting(key: String): BaseResponse<GlobalSetting?> {
        return settingService.globalSetting(key)
    }
    suspend fun multiProH(): BaseResponse<List<MultiP>?> {
        return service.multiProH()
    }

}