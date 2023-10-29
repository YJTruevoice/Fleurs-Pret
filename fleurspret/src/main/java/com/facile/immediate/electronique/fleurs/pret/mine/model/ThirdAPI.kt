package com.facile.immediate.electronique.fleurs.pret.mine.model

import com.facile.immediate.electronique.fleurs.pret.common.UserManager
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ThirdAPI {
    @FormUrlEncoded
    @POST("/fleurspret/few/aimCompressedSpain")
    suspend fun getUserInfoBasic(@Field(NetMgr.CommonParamsKey.USER_ID) uid: String = UserManager.userId()): BaseResponse<UserBasicEntity?>

    @FormUrlEncoded
    @POST("/fleurspret/few/letCubicGlobe")
    suspend fun getUserHeadPortrait(@Field(NetMgr.CommonParamsKey.USER_ID) uid: String = UserManager.userId()): BaseResponse<UserBasicEntity?>
}