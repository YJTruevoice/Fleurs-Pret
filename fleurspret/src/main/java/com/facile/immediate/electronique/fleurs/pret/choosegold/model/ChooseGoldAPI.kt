package com.facile.immediate.electronique.fleurs.pret.choosegold.model

import com.facile.immediate.electronique.fleurs.pret.common.UserManager
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ChooseGoldAPI {

    @FormUrlEncoded
    @POST("/fleurspret/extraordinaryNew/multiplyDeepDatabase")
    suspend fun prodList(@Field(NetMgr.CommonParamsKey.USER_ID) uid: String = UserManager.userId()): BaseResponse<ProdList?>

    @FormUrlEncoded
    @POST("/fleurspret/extraordinaryNew/relaySafeArticle")
    suspend fun preCompute(
        @Field("activeAsianBookcase") activeAsianBookcase: String,
        @Field("neatPhysicsPeasantCommonSport") neatPhysicsPeasantCommonSport: String,
        @Field("rudeHungryActionInformation") rudeHungryActionInformation: String,
    ): BaseResponse<PreCompResult?>

    @FormUrlEncoded
    @POST("/fleurspret/victory/reuseRectangleAssistant")
    suspend fun preOrd(
        @Field("activeAsianBookcase") activeAsianBookcase: String,
        @Field("neatPhysicsPeasantCommonSport") neatPhysicsPeasantCommonSport: String,
        @Field("rudeHungryActionInformation") rudeHungryActionInformation: String,
    ): BaseResponse<OrdInfor?>

    @FormUrlEncoded

    @POST("/fleurspret/victory/developHorribleFaith")
    suspend fun submitOrd(
        @Field("activeAsianBookcase") activeAsianBookcase: String,
        @Field("neatPhysicsPeasantCommonSport") neatPhysicsPeasantCommonSport: String,
        @Field("rudeHungryActionInformation") rudeHungryActionInformation: String,
        @Field("normalBillClinicMercifulBay") normalBillClinicMercifulBay: String,
    ): BaseResponse<Any?>

}