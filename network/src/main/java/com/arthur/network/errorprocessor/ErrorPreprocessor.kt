package com.arthur.network.errorprocessor

import com.arthur.network.NetConstant


/**
 * 网络请求错误处理
 */
object ErrorPreprocessor {

    /**
     * ##是否为网络异常
     * 以下6种code归为网络异常
     *  *  [NetConstant.ErrorCode.ERROR_NETWORK_TIMEOUT]
     *  *  [NetConstant.ErrorCode.ERROR_HTTP]
     *  *  [NetConstant.ErrorCode.ERROR_NETWORK_CONNECT]
     *  *  [NetConstant.ErrorCode.ERROR_NETWORK_UNKNOWN_HOST]
     *  *  [NetConstant.ErrorCode.ERROR_SOCKET]
     *  *  [NetConstant.ErrorCode.ERROR_SSL]
     */
    fun isNetError(ec: Int): Boolean {
        return ec == NetConstant.ErrorCode.ERROR_NETWORK_TIMEOUT
                || ec == NetConstant.ErrorCode.ERROR_HTTP
                || ec == NetConstant.ErrorCode.ERROR_NETWORK_CONNECT
                || ec == NetConstant.ErrorCode.ERROR_NETWORK_UNKNOWN_HOST
                || ec == NetConstant.ErrorCode.ERROR_SOCKET
                || ec == NetConstant.ErrorCode.ERROR_SSL
    }
}