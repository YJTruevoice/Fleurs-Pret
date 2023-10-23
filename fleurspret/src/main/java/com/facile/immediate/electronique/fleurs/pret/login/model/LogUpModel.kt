package com.facile.immediate.electronique.fleurs.pret.login.model

import com.arthur.baselib.structure.base.IBaseModel
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import retrofit2.http.Field

class LogUpModel : IBaseModel {
    private val service = NetMgr.get().service<LogUpAPI>()

    suspend fun getVerifyCode(radioactiveCheekChalkDeliciousAirmail: String): BaseResponse<VerifyCodeEntity?> {
        return service.getVerifyCode(radioactiveCheekChalkDeliciousAirmail)
    }

    suspend fun logUp(
        // no
        radioactiveCheekChalkDeliciousAirmail: String,
        // code
        dearProperArgument: String,
        // gaid
        steadyRepairsAsianClinicTiredAgriculture: String = "",
        // app instance id
        dearCrimeCellar: String = ""
    ): BaseResponse<UserInfoEntity?> {
        return service.logUp(
            radioactiveCheekChalkDeliciousAirmail,
            dearProperArgument,
            steadyRepairsAsianClinicTiredAgriculture,
            dearCrimeCellar
        )
    }
}