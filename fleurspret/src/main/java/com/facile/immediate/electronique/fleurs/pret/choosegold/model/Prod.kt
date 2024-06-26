package com.facile.immediate.electronique.fleurs.pret.choosegold.model


import android.os.Parcelable
import com.arthur.commonlib.utils.DateUtil
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProdList(
    @SerializedName("dampCabbageMaximumSorryCabbage")
    val dampCabbageMaximumSorryCabbage: String = DateUtil.getNow().time.toString(),
    @SerializedName("activeAsianBookcase")
    val activeAsianBookcase: String = "",
    @SerializedName("facialGardenFamiliarPossession")
    val facialGardenFamiliarPossession: String = "",
    @SerializedName("bornSunglassesRipeProblemFalseHeadmaster")
    val bornSunglassesRipeProblemFalseHeadmaster: ArrayList<ProdInfo>? = null,
) : Parcelable

@Parcelize
data class ProdInfo(
    @SerializedName("neatPhysicsPeasantCommonSport")
    val neatPhysicsPeasantCommonSport: String = "",
    @SerializedName("plainLungAppleGale")
    val plainLungAppleGale: Long = 0,
    @SerializedName("passiveChairwomanSurroundingArm")
    val passiveChairwomanSurroundingArm: Float = 0.0f,
    @SerializedName("afraidDecemberSlimClassicalTechnology")
    val afraidDecemberSlimClassicalTechnology: Int = 0,
    @SerializedName("backBenchRegularHomeland")
    val backBenchRegularHomeland: Float = 1000f,
    @SerializedName("hugeFogPepper")
    val hugeFogPepper: Int = 0
) : Parcelable, RepayDate() {
    @IgnoredOnParcel
    var dampCabbageMaximumSorryCabbage = ""

    @IgnoredOnParcel
    var optTerm = 0

    val dateStr: String
        get() {
            val durMillis: Long =
                ((if (UserManager.isTestAccount()) 0 else plainLungAppleGale) + optTerm) * 1000 * 60 * 60 * 24
            return DateUtil.getDateWithFormat(
                DateUtil.targetTimeMillis(dampCabbageMaximumSorryCabbage) + durMillis,
                "dd-MM-yyyy"
            )
        }
}