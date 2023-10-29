package com.facile.immediate.electronique.fleurs.pret.home.model

import com.arthur.baselib.structure.base.IBaseModel
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr

class FirstModel : IBaseModel {
    private val service: FirstAPI = NetMgr.get().service()

    suspend fun multiOrSingle(): BaseResponse<List<MultiOrSingle>?> {
        return service.multiOrSingle()
    }

    suspend fun singleProH(key: String): BaseResponse<GlobalInfo?> {
        return service.singleProH(key)
    }
    suspend fun multiProH(): BaseResponse<List<MultiP>?> {
        return service.multiProH()
    }

}