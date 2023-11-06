package com.facile.immediate.electronique.fleurs.pret.common.config

import com.facile.immediate.electronique.fleurs.pret.input.model.Region
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ConfigAPI {
    @FormUrlEncoded
    @POST("/fleurspret/religiousLawyer/mopMaximumDevotion")
    suspend fun config(@Field("boringSufferingLooseReasonAggressivePhysicist") type: String): BaseResponse<List<CommonConfigItem>?>

    @FormUrlEncoded
    @POST("/fleurspret/religiousLawyer/regardMetalChapter")
    suspend fun region(
        @Field("readyBoringThemselvesBigMovie") regionParentId: String,
        @Field("saltySheCitizen") regionlevel: String
    ): BaseResponse<List<Region>?>
}