package com.facile.immediate.electronique.fleurs.pret.input.model

import com.facile.immediate.electronique.fleurs.pret.common.user.UserBasicEntity
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface InputAPI {

    @FormUrlEncoded
    @POST("/fleurspret/few/aimCompressedSpain")
    suspend fun preInputInfo(@Field("necessaryRapidCustoms") pageType: Int): BaseResponse<UserBasicEntity?>

    @FormUrlEncoded
    @POST("/fleurspret/few/holdPoorSpeaker")
    suspend fun savePersonalInfo(
        @Field("necessaryRapidCustoms") pageType: Int,
        @Field("dirtyCrowdedEarthquakePrivateLevel") names: String,
        @Field("dustyChocolateEaster") surnames: String,
        @Field("messyChapterLemonDozen") birthDay: String,
        @Field("sureChemistryBigFairness") sex: String,
        @Field("moralPressure") email: String,
        @Field("mercifulVanillaMatchBitterFirewood") mercifulVanillaMatchBitterFirewood: String,
        @Field("neitherSeniorStocking") neitherSeniorStocking: String,
        @Field("australianHandsomeSummer") australianHandsomeSummer: String,
        @Field("dangerousGoatContraryDueSemicircle") fullAddress: String
    ): BaseResponse<Any?>

    @FormUrlEncoded
    @POST("/fleurspret/few/holdPoorSpeaker")
    suspend fun saveContactInfo(
        @Field("necessaryRapidCustoms") pageType: Int,
        @Field("usualExtraordinaryScholarshipQuickHardship") phoneNumber: String,
        @Field("irishGradeUndergroundAmericanPostcard") name: String,
        @Field("constantNovelistMessSureLibrary") relationship: String,
        @Field("europeanBeardUniform") phoneNumberSec: String,
        @Field("theseMedicalRadioactiveDoll") nameSec: String,
        @Field("mostSpaceshipVideophoneBirdcage") relationshipSec: String
    ): BaseResponse<Any?>

    @FormUrlEncoded
    @POST("/fleurspret/few/holdPoorSpeaker")
    suspend fun saveIdentityInfo(
        @Field("necessaryRapidCustoms") pageType: Int,
        @Field("radioactiveCheekChalkDeliciousAirmail") energeticRudePollutionVisitor: String,
        @Field("gratefulTourismFool") idCardNo: String
    ): BaseResponse<Any?>

    @FormUrlEncoded
    @POST("/fleurspret/few/setChineseSystem")
    suspend fun gatheringInfo(@Field("necessaryRapidCustoms") pageType: Int = 5): BaseResponse<List<GatheringInfo>?>

    @FormUrlEncoded
    @POST("/fleurspret/few/parkAllDustbin")
    suspend fun saveGatheringInfo(@FieldMap map: Map<String, String>): BaseResponse<Any?>


    /**
     * 查询身份证人脸图片
     */
    @FormUrlEncoded
    @POST("/fleurspret/few/letCubicGlobe")
    suspend fun identityAfrPic(@Field("necessaryRapidCustoms") pageType: Int): BaseResponse<IdentityPic?>

}