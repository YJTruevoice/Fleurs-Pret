package com.facile.immediate.electronique.fleurs.pret.login.model

import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LogUpAPI {
    @FormUrlEncoded
    @POST("/fleurspret/blueBrake/saveNecessarySalary")
    suspend fun getVerifyCode(
        @Field("radioactiveCheekChalkDeliciousAirmail") radioactiveCheekChalkDeliciousAirmail: String
    ): BaseResponse<VerifyCodeEntity?>

    @FormUrlEncoded
    @POST("/fleurspret/blueBrake/robCoolLawyer")
    suspend fun logUp(
        @Field("radioactiveCheekChalkDeliciousAirmail") radioactiveCheekChalkDeliciousAirmail: String,
        @Field("dearProperArgument") dearProperArgument: String,
        @Field("steadyRepairsAsianClinicTiredAgriculture") steadyRepairsAsianClinicTiredAgriculture: String,
        @Field("dearCrimeCellar") dearCrimeCellar: String
    ): BaseResponse<UserInfoEntity?>
}