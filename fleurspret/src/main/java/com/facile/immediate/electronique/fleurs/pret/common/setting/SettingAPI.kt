package com.facile.immediate.electronique.fleurs.pret.common.setting

import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerEntity
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SettingAPI {
    @FormUrlEncoded
    @POST("/fleurspret/religiousLawyer/parkDeadEquipment")
    suspend fun globalSetting(@Field("solidAugustGuard") key: String): BaseResponse<GlobalSetting?>

    @FormUrlEncoded
    @POST("/fleurspret/few/meetNewIreland")
    suspend fun recommendBanner(@Field(NetMgr.CommonParamsKey.USER_ID) uid: String = UserManager.userId()): BaseResponse<RecommendBanner?>

    @FormUrlEncoded
    @POST("/fleurspret/religiousLawyer/cureEnglishDecember")
    suspend fun consumerConfig(@Field(NetMgr.CommonParamsKey.APP_SSID) halfPainfulEverything: String = NetMgr.CommonValue.DISK_NUM): BaseResponse<ConsumerEntity?>
}