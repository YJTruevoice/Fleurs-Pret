package com.arthur.network.exception

import com.arthur.network.NetConstant
import java.io.IOException

/**
 *  * @Author Arthur
 *  * @Data 2022/8/2
 *  * @Description
 *  ## 自定义网络请求异常
 */
open class NetBaseException(msg : String?) : IOException(msg) {
    open var errorCode = NetConstant.ErrorCode.ERROR_UNKNOWN
    open var responseData: String? = null

    constructor(msg: String?, eCode: Int) : this(msg) {
        errorCode = eCode
    }

    constructor(msg: String?, eCode: Int, response: String?) : this(msg) {
        errorCode = eCode
        responseData = response
    }
}