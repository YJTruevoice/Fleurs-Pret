package com.facile.immediate.electronique.fleurs.pret.choosegold.model


import com.google.gson.annotations.SerializedName

data class OrdInfor(
    @SerializedName("cottonGoodData")
    val cottonGoodData: String? = null,
    @SerializedName("flatFoggyGlasshouseMerrySuffering")
    val flatFoggyGlasshouseMerrySuffering: String? = null,
    @SerializedName("illTerm")
    val illTerm: String? = null,
    @SerializedName("nationalTimetableFirewood")
    val nationalTimetableFirewood: String? = null,
    @SerializedName("normalBillClinicMercifulBay")
    val normalBillClinicMercifulBay: String? = null,
    @SerializedName("similarDealElectronicKeyboard")
    val similarDealElectronicKeyboard: String? = null,
    @SerializedName("unknownReasonBlueHostess")
    val unknownReasonBlueHostess: List<Contract>? = null
)

data class Contract(
    @SerializedName("eitherPowerLamp")
    val eitherPowerLamp: String? = null,
    @SerializedName("emptyIceland")
    val emptyIceland: String? = null,
    @SerializedName("irishGradeUndergroundAmericanPostcard")
    val irishGradeUndergroundAmericanPostcard: String? = null
)