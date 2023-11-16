package com.facile.immediate.electronique.fleurs.pret.input.model

import com.facile.immediate.electronique.fleurs.pret.mine.model.UserBasicEntity
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import retrofit2.http.Field
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
        @Field("radioactiveCheekChalkDeliciousAirmail") phoneNo: String,
        @Field("distantGratefulStove") idCardNo: String
    ): BaseResponse<Any?>

    @FormUrlEncoded
    @POST("/fleurspret/few/setChineseSystem")
    suspend fun gatheringInfo(): BaseResponse<GatheringInfo?>

    /**
    bankCode    [string]	是	数据字典bankNameList对应的code
    bankName    [string]	是	数据字典bankNameList对应的value
    bankAccountNumber    [string]	是	输入的银行卡号
    bankAccountType    [string]	是	数据字典bankAccountType对应的code
    bankCardUrl    [string]	否	上传银行卡成功返回的url, 或查询银行卡图片接口返回的bankCardImagOrgUrl
    pageType    [string]	是	传值 5 （int 类型）
     */
    @FormUrlEncoded
    @POST("/fleurspret/few/parkAllDustbin")
    suspend fun saveGatheringInfo(
        @Field("necessaryRapidCustoms") pageType: Int,
        @Field("unablePolePacificShop") bankCode: String,
        @Field("nativeShirtGrocerYesterday") bankName: String,
        @Field("lastBuildingTroublesomeRainbowChapter") bankAccountNumber: String,
        @Field("looseManSauce") bankAccountType: String,
    ): BaseResponse<Any?>


    /**
     * 查询身份证人脸图片
     */
    @FormUrlEncoded
    @POST("/fleurspret/few/letCubicGlobe")
    suspend fun identityAfrPic(@Field("necessaryRapidCustoms") pageType: Int): BaseResponse<IdentityPic?>

}