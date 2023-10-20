package com.arthur.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 *  * @Author Arthur
 *  * @Data 2022/8/2
 *  * @Description
 *  ## 网络响应体 base
 */
@Parcelize
open class NetResponse(
    val data: @RawValue Any? = null
) : NetBaseResponse(), Parcelable
