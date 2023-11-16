package com.facile.immediate.electronique.fleurs.pret.choosegold.model

import com.arthur.baselib.structure.base.IBaseModel
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr

class ChooseGoldModel : IBaseModel {

    private val service = NetMgr.get().service<ChooseGoldAPI>()


    suspend fun prodList(): BaseResponse<ProdList?> {
        return service.prodList()
    }

    suspend fun preCompute(
        activeAsianBookcase: String,
        neatPhysicsPeasantCommonSport: String,
        rudeHungryActionInformation: String
    ): BaseResponse<PreCompResult?> {
        return service.preCompute(
            activeAsianBookcase,
            neatPhysicsPeasantCommonSport,
            rudeHungryActionInformation
        )
    }

    suspend fun preOrd(
        activeAsianBookcase: String,
        neatPhysicsPeasantCommonSport: String,
        rudeHungryActionInformation: String
    ): BaseResponse<OrdInfor?> {
        return service.preOrd(
            activeAsianBookcase,
            neatPhysicsPeasantCommonSport,
            rudeHungryActionInformation
        )
    }

    suspend fun submitOrd(
        activeAsianBookcase: String,
        neatPhysicsPeasantCommonSport: String,
        rudeHungryActionInformation: String,
        normalBillClinicMercifulBay: String
    ): BaseResponse<Any?> {
        return service.submitOrd(
            activeAsianBookcase,
            neatPhysicsPeasantCommonSport,
            rudeHungryActionInformation,
            normalBillClinicMercifulBay
        )
    }
}