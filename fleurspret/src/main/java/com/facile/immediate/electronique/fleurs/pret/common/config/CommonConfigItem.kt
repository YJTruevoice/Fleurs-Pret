package com.facile.immediate.electronique.fleurs.pret.common.config


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
open class CommonConfigItem(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("value")
    val value: String = ""
) : Parcelable