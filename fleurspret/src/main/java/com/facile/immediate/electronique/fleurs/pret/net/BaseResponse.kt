package com.facile.immediate.electronique.fleurs.pret.net

import android.os.Parcelable
import com.arthur.network.model.NetBaseResponse
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class BaseResponse<T>(
    val greekCompetitorSquareBirdcage: Int = -1,
    val civilGoatNearEveryMoustache: String = "",
    val aggressiveParentMethod: @RawValue T? = null
) : Parcelable, NetBaseResponse()