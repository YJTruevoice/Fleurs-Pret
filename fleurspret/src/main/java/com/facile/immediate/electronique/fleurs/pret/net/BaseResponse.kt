package com.facile.immediate.electronique.fleurs.pret.net

import android.os.Parcelable
import com.arthur.network.model.NetBaseResponse
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class BaseResponse<T>(
    val data: @RawValue T? = null
) : Parcelable, NetBaseResponse()