package com.facile.immediate.electronique.fleurs.pret.order.model

import com.facile.immediate.electronique.fleurs.pret.common.UserManager
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OrdAPI {

    @FormUrlEncoded
    @POST("/fleurspret/science/fixJustDiet")
    suspend fun ordArr(@Field(NetMgr.CommonParamsKey.USER_ID) uid: String = UserManager.userId()): BaseResponse<List<Ord>?>
}