package com.facile.immediate.electronique.fleurs.pret.loan.model

import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoanAPI {
    @FormUrlEncoded
    @POST("/fleurspret/religiousLawyer/drawConceitedFlashlight")
    suspend fun checkOrdInfo(@Field(NetMgr.CommonParamsKey.USER_ID) uid: String = UserManager.userId()): BaseResponse<OrdStateInfo?>

    @FormUrlEncoded
    @POST("/fleurspret/generalNotebook/behaveChangeableScientist")
    suspend fun getPayList(@Field("boringSufferingLooseReasonAggressivePhysicist") boringSufferingLooseReasonAggressivePhysicist: String = "00"): BaseResponse<List<PayType>?>

    @FormUrlEncoded
    @POST("/fleurspret/generalNotebook/praiseRegularDowntown")
    suspend fun getPayLink(
        @Field("normalBillClinicMercifulBay") normalBillClinicMercifulBay: String,
        @Field("metalCancerVirtueRainfall") metalCancerVirtueRainfall: String,
        @Field("expensiveRadioGreyPetScientificSystem") expensiveRadioGreyPetScientificSystem: String,
        @Field("someMidnightThisSentenceEnglishQuilt") someMidnightThisSentenceEnglishQuilt: String = ""
    ): BaseResponse<PayLink?>
    @FormUrlEncoded
    @POST("/fleurspret/victory/lackConsiderateSituation")
    suspend fun getPayDt(
        @Field("normalBillClinicMercifulBay") normalBillClinicMercifulBay: String,
        @Field("metalCancerVirtueRainfall") metalCancerVirtueRainfall: String
    ): BaseResponse<PayDetail?>


}