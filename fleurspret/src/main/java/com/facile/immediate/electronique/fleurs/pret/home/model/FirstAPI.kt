package com.facile.immediate.electronique.fleurs.pret.home.model

import com.facile.immediate.electronique.fleurs.pret.common.UserManager
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FirstAPI {

    @FormUrlEncoded
    @POST("/fleurspret/dampFrost/mustLiquidTrial")
    suspend fun multiOrSingle(@Field(NetMgr.CommonParamsKey.USER_ID) uid: String = UserManager.userId()): BaseResponse<List<ProInfo>?>

    @FormUrlEncoded
    @POST("/fleurspret/dampFrost/mustLiquidTrial")
    suspend fun multiProH(@Field(NetMgr.CommonParamsKey.USER_ID) uid: String = UserManager.userId()): BaseResponse<List<MultiP>?>
}