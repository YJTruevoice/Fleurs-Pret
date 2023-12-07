package com.arthur.network.exception

import com.arthur.network.Net
import com.arthur.network.NetConstant

/**
 * @Author Arthur
 * @Data 2022/9/30
 * @Description
 * 请求成功后数据不合规
 */
class IllegalDataException(
    override var errorCode: Int = NetConstant.ErrorCode.ERROR_ILLEGAL_DATA,
    val msg: String = Net.client.netOptions.tipDataIllegalError
        ?: NetConstant.ErrorPromptMsg.DATA_ILLEGAL_ERROR
) : NetBaseException(msg)