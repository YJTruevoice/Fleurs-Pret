package com.facile.immediate.electronique.fleurs.pret.input.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
bankNameDesc	[string]	否	银行名称, Name of the bank, Nombre del banco
bankAccountTypeDesc	[string]	否	银行账户类型, Bank account type, Tipo de cuenta bancaria
bankAccountType	[string]	否	保存数据时, 数据字典bankAccountType对应的code
bankAccountNumber	[string]	否	银行帐号, Bank account number, Número de cuenta bancaria
bankName	[string]	否	银行名称, 保存数据时, 数据字典bankNameList对应的value
bankCode	[string]	否	保存数据时, 数据字典bankNameList对应的code
 */
@Parcelize
data class GatheringInfo(
    @SerializedName("unablePolePacificShop")
    val unablePolePacificShop: String? = null,
    @SerializedName("incorrectCoursebookIcelandPrinter")
    val incorrectCoursebookIcelandPrinter: String? = null,
    @SerializedName("looseManSauce")
    val looseManSauce: String? = null,
    @SerializedName("brokenPupilCough")
    val brokenPupilCough: String? = null,
    @SerializedName("lastBuildingTroublesomeRainbowChapter")
    val lastBuildingTroublesomeRainbowChapter: String? = null,

    @SerializedName("ancientRapidMetalSauce")
    val ancientRapidMetalSauce: String? = null,
    @SerializedName("australianGuiltyFridayBattery")
    val australianGuiltyFridayBattery: String? = null,
    @SerializedName("centralTrolleyStandardHillsideAche")
    val centralTrolleyStandardHillsideAche: String? = null,
    @SerializedName("frequentChairwomanJob")
    val frequentChairwomanJob: String? = null,
    @SerializedName("gratefulTourismFool")
    val gratefulTourismFool: String? = null,
    @SerializedName("hardworkingBestTail")
    val hardworkingBestTail: String? = null,
    @SerializedName("hopefulMicroscope")
    val hopefulMicroscope: String? = null,
    @SerializedName("metalCancerVirtueRainfall")
    val metalCancerVirtueRainfall: String? = null,
    @SerializedName("nativeShirtGrocerYesterday")
    val nativeShirtGrocerYesterday: String? = null,
    @SerializedName("nextNovelPuzzledCouple")
    val nextNovelPuzzledCouple: String? = null,
    @SerializedName("pinkPostcardSpoonScene")
    val pinkPostcardSpoonScene: String? = null,
    @SerializedName("plainSameInventor")
    val plainSameInventor: String? = null,
    @SerializedName("rawManagerDryer")
    val rawManagerDryer: String? = null,
    @SerializedName("safeVirtueClinicNewJewel")
    val safeVirtueClinicNewJewel: String? = null,
    @SerializedName("straightCandleBornAnyoneRareAccent")
    val straightCandleBornAnyoneRareAccent: String? = null,
    @SerializedName("uglyParticularBirthplace")
    val uglyParticularBirthplace: String? = null
) : Parcelable