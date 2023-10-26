package com.facile.immediate.electronique.fleurs.pret.mine.model

import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ThirdAPI {
    @FormUrlEncoded
    @POST("/fleurspret/few/aimCompressedSpain")
    suspend fun getUserInfoBasic(): BaseResponse<UserBasicEntity?>

    @FormUrlEncoded
    @POST("/fleurspret/few/letCubicGlobe")
    suspend fun getUserHeadPortrait(): BaseResponse<UserBasicEntity?>
}