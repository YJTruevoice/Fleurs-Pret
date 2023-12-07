package com.facile.immediate.electronique.fleurs.pret.common.config


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
open class CommonConfigItem(
    @SerializedName("greekCompetitorSquareBirdcage")
    val code: String = "",
    @SerializedName("egyptianHeight")
    val value: String = "",

    var next: CommonConfigItem? = null
) : Parcelable