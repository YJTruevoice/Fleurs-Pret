package com.facile.immediate.electronique.fleurs.pret.order.model

import com.arthur.baselib.structure.base.IBaseModel
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr

class OrdModel : IBaseModel {
    private val service = NetMgr.get().service<OrdAPI>()

    suspend fun ordArr(): BaseResponse<List<Ord>?> {
        return service.ordArr()
    }
}