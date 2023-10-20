package com.arthur.network.errorprocessor

import androidx.annotation.CallSuper
import com.google.gson.JsonParseException
import com.arthur.network.NetConstant
import com.arthur.network.exception.IllegalDataException
import com.arthur.network.exception.NetBaseException
import com.arthur.network.model.ErrorInfo
import com.arthur.network.model.ErrorType
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 *  * @Author Arthur
 *  * @Data 2022/9/30
 *  * @Description
 * ## base网络请求错误处理器
 */
open class BaseApiErrorPreprocessor : IApiErrorPreprocessor {

    @CallSuper
    override fun processError(error: Throwable?): ErrorInfo {
        return ErrorInfo().apply {
            when (error) {
                is HttpException -> {// Retrofit的HttpException
                    errorType = ErrorType.HTTP_ERROR
                    errorCode = error.code()
                    errorMsg = error.message()
                    result = error.response().toString()
                }
                is SSLException -> {
                    errorType = ErrorType.HTTP_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_SSL
                    errorMsg = NetConstant.ErrorPromptMsg.CONNECT_ERROR_OTHER
                }
                is UnknownHostException -> {
                    errorType = ErrorType.HTTP_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_NETWORK_UNKNOWN_HOST
                    errorMsg = NetConstant.ErrorPromptMsg.CONNECT_ERROR_OTHER
                }
                is IllegalArgumentException -> {
                    errorType = ErrorType.HTTP_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_HTTP
                    errorMsg = NetConstant.ErrorPromptMsg.CONNECT_ERROR_OTHER
                }
                is ConnectException -> {
                    errorType = ErrorType.CONNECT_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_NETWORK_CONNECT
                    errorMsg = NetConstant.ErrorPromptMsg.CONNECT_ERROR
                }
                is SocketException -> {
                    errorType = ErrorType.CONNECT_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_SOCKET
                    errorMsg = NetConstant.ErrorPromptMsg.CONNECT_ERROR
                }
                is SocketTimeoutException -> {
                    errorType = ErrorType.CONNECT_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_NETWORK_TIMEOUT
                    errorMsg = NetConstant.ErrorPromptMsg.SERVICE_TIMEOUT
                }

                is JsonParseException -> {
                    errorType = ErrorType.PARSE_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_PARSE
                    errorMsg = NetConstant.ErrorPromptMsg.DATA_PARSE_ERROR
                }

                is IllegalDataException -> {
                    errorType = ErrorType.CUSTOM_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_ILLEGAL_DATA
                    errorMsg = error.msg
                }

                is NetBaseException -> {
                    errorType = ErrorType.CUSTOM_ERROR
                    errorCode = error.errorCode
                    errorMsg = error.message ?: "发生错误$errorType"
                    result = error.responseData ?: ""
                }

                else -> {
                    errorType = ErrorType.CUSTOM_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_UNKNOWN
                    errorMsg = "未知异常"
                }
            }
        }
    }
}