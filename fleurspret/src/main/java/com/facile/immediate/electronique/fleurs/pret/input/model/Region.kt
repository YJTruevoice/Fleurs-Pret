package com.facile.immediate.electronique.fleurs.pret.input.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
"eastBasicFavouriteSupermarket": "regionId",
"normalAppointmentHeadmistressMachine": "regionName",
"readyBoringThemselvesBigMovie": "regionParentId",
"musicalHistoryPracticalFurnishedCost": "regionLevel",
"noblePileTheseFatTongue": "zipCode"
 */
@Parcelize
data class Region(
    val eastBasicFavouriteSupermarket: String = "",
    val normalAppointmentHeadmistressMachine: String = "",
    val readyBoringThemselvesBigMovie: String = "",
    val musicalHistoryPracticalFurnishedCost: String = "",
    val noblePileTheseFatTongue: String = "",
) : Parcelable {
    @IgnoredOnParcel
    var selected: Boolean = false
}
