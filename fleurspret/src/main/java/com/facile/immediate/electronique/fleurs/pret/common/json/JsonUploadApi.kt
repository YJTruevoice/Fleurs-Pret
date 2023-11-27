package com.facile.immediate.electronique.fleurs.pret.common.json

import com.arthur.commonlib.utils.SPUtils
import com.facile.immediate.electronique.fleurs.pret.common.json.model.JSONNeed
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface JsonUploadApi {
    @FormUrlEncoded
    @POST("/fleurspret/spirit/bringFarLead")
    suspend fun jsonNeed(
        @Field(NetMgr.CommonParamsKey.GOOGLE_AD_ID) gai: String = SPUtils.getString(
            NetMgr.CommonParamsKey.GOOGLE_AD_ID,
            defaultValue = ""
        )
    ): BaseResponse<JSONNeed?>

    @POST("/fleurspret/spirit/followDearSalad")
    suspend fun jsonFeature(@Body requestBody: RequestBody): BaseResponse<Any?>
}