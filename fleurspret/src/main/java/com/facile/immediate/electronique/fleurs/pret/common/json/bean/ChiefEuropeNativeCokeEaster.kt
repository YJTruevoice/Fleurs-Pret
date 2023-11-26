package com.facile.immediate.electronique.fleurs.pret.common.json.bean


import com.google.gson.annotations.SerializedName

data class ChiefEuropeNativeCokeEaster(
    @SerializedName("friendlyDecorationSuchSoutheast")
    val friendlyDecorationSuchSoutheast: FriendlyDecorationSuchSoutheast = FriendlyDecorationSuchSoutheast(),
    @SerializedName("holyChemicalDisaster")
    val holyChemicalDisaster: String = "",
    @SerializedName("rareCartoonChemicalChimneyToday")
    val rareCartoonChemicalChimneyToday: String = "",
    @SerializedName("sharpCompetitorExactScottishAirport")
    val sharpCompetitorExactScottishAirport: List<SharpCompetitorExactScottishAirport> = listOf()
)