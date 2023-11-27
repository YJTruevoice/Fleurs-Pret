package com.facile.immediate.electronique.fleurs.pret.common.json.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JSONNeed(
    @SerializedName("agriculturalLidSingleFurnishedBush")
    val agriculturalLidSingleFurnishedBush: String = ""
) : Parcelable