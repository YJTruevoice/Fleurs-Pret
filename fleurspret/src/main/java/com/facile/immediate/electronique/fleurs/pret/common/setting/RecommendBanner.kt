package com.facile.immediate.electronique.fleurs.pret.common.setting


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendBanner(
    val unableDam: String? = null,
    @SerializedName("deepInterpreterPoliteFreshElectricity")
    val deepInterpreterPoliteFreshElectricity: List<RecommendPro>? = null
) : Parcelable

@Parcelize
data class RecommendPro(
    @SerializedName("arabicNephewNoisyZooNoisyBaby")
    val arabicNephewNoisyZooNoisyBaby: String? = null,
    @SerializedName("chineseGeographyTenseHopefulSpirit")
    val chineseGeographyTenseHopefulSpirit: String? = null,
    @SerializedName("foggyEveryoneLivingArmyPrinter")
    val foggyEveryoneLivingArmyPrinter: String? = null
) : Parcelable