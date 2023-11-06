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


}