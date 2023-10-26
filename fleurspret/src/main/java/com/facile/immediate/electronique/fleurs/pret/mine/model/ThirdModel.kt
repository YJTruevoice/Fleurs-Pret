package com.facile.immediate.electronique.fleurs.pret.mine.model

import com.arthur.baselib.structure.base.IBaseModel
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr

class ThirdModel : IBaseModel {
    private val service = NetMgr.get().service<ThirdAPI>()

    suspend fun getUserInfoBasic(): BaseResponse<UserBasicEntity?> {
        return service.getUserInfoBasic()
    }

    suspend fun getUserHeadPortrait(): BaseResponse<UserBasicEntity?> {
        return service.getUserHeadPortrait()
    }
}