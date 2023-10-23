package com.facile.immediate.electronique.fleurs.pret.home.model

import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FirstAPI {

    @FormUrlEncoded
    @POST("/fleurspret/religiousLawyer/parkDeadEquipment")
    suspend fun appSetting(@Field("solidAugustGuard") key: String): BaseResponse<GlobalInfo?>
}