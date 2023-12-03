package com.facile.immediate.electronique.fleurs.pret.loan.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PayType(
    @SerializedName("dailyZeroExcellentJewelRubber")
    val dailyZeroExcellentJewelRubber: String = "",
    @SerializedName("expensiveRadioGreyPetScientificSystem")
    val expensiveRadioGreyPetScientificSystem: String = "",
    @SerializedName("localLuckyHomeland")
    val localLuckyHomeland: String = "",
    @SerializedName("someMidnightThisSentenceEnglishQuilt")
    val someMidnightThisSentenceEnglishQuilt: String = "",
    @SerializedName("unfitCanadianSeat")
    val unfitCanadianSeat: String = ""
) : Parcelable