package com.facile.immediate.electronique.fleurs.pret.loan.model

import com.facile.immediate.electronique.fleurs.pret.common.UserManager
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoanAPI {
    @FormUrlEncoded
    @POST("/fleurspret/religiousLawyer/drawConceitedFlashlight")
    suspend fun checkOrdInfo(@Field(NetMgr.CommonParamsKey.USER_ID) uid: String = UserManager.userId()): BaseResponse<OrdStateInfo?>
}