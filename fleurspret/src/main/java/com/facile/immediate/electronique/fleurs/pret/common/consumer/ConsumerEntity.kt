package com.facile.immediate.electronique.fleurs.pret.common.consumer


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConsumerEntity(
    @SerializedName("lazyNervousToiletHam")
    val lazyNervousToiletHam: List<ItemEntity>? = null,
    @SerializedName("favouriteHungerLazyCoach")
    val favouriteHungerLazyCoach: List<ItemEntity>? = null,
    @SerializedName("freezingFieldPostmanSpaghetti")
    val freezingFieldPostmanSpaghetti: List<ItemEntity>? = null,
    @SerializedName("looseArabicCustom")
    val looseArabicCustom: String? = null
) : Parcelable

@Parcelize
data class ItemEntity(
    @SerializedName("boringSufferingLooseReasonAggressivePhysicist")
    val boringSufferingLooseReasonAggressivePhysicist: String? = null,
    @SerializedName("egyptianHeight")
    val egyptianHeight: String? = null
) : Parcelable