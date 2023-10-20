package com.arthur.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *  * @Author Arthur
 *  * @Data 2023/4/18
 *  * @Description
 * ## 网络响应体 base 值提供code和msg
 */
@Parcelize
open class NetBaseResponse(
    val msg: String = "",
    val code: Int = -1,
) : Parcelable