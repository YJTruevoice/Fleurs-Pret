package com.arthur.network.errorprocessor

import androidx.annotation.CallSuper
import com.arthur.network.Net
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
                    errorMsg = Net.client.netOptions.tipConnectErrorOther
                        ?: NetConstant.ErrorPromptMsg.CONNECT_ERROR_OTHER
                }

                is UnknownHostException -> {
                    errorType = ErrorType.HTTP_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_NETWORK_UNKNOWN_HOST
                    errorMsg = Net.client.netOptions.tipConnectErrorOther
                        ?: NetConstant.ErrorPromptMsg.CONNECT_ERROR_OTHER
                }

                is IllegalArgumentException -> {
                    errorType = ErrorType.HTTP_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_HTTP
                    errorMsg = Net.client.netOptions.tipConnectErrorOther
                        ?: NetConstant.ErrorPromptMsg.CONNECT_ERROR_OTHER
                }

                is ConnectException -> {
                    errorType = ErrorType.CONNECT_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_NETWORK_CONNECT
                    errorMsg = Net.client.netOptions.tipConnectError
                        ?: NetConstant.ErrorPromptMsg.CONNECT_ERROR
                }

                is SocketException -> {
                    errorType = ErrorType.CONNECT_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_SOCKET
                    errorMsg = Net.client.netOptions.tipConnectError
                        ?: NetConstant.ErrorPromptMsg.CONNECT_ERROR
                }

                is SocketTimeoutException -> {
                    errorType = ErrorType.CONNECT_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_NETWORK_TIMEOUT
                    errorMsg = Net.client.netOptions.tipServiceTimeout
                        ?: NetConstant.ErrorPromptMsg.SERVICE_TIMEOUT
                }

                is JsonParseException -> {
                    errorType = ErrorType.PARSE_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_PARSE
                    errorMsg = Net.client.netOptions.tipDataParseError
                        ?: NetConstant.ErrorPromptMsg.DATA_PARSE_ERROR
                }

                is IllegalDataException -> {
                    errorType = ErrorType.CUSTOM_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_ILLEGAL_DATA
                    errorMsg = error.msg
                }

                is NetBaseException -> {
                    errorType = ErrorType.CUSTOM_ERROR
                    errorCode = error.errorCode
                    errorMsg = error.message
                        ?: "${Net.client.netOptions.tipDefaultBusinessError ?: NetConstant.ErrorPromptMsg.DEFAULT_BUSINESS_ERROR}$errorType"
                    result = error.responseData ?: ""
                }

                else -> {
                    errorType = ErrorType.CUSTOM_ERROR
                    errorCode = NetConstant.ErrorCode.ERROR_UNKNOWN
                    errorMsg = Net.client.netOptions.tipUnknownError
                        ?: NetConstant.ErrorPromptMsg.UNKNOWN_ERROR
                }
            }
        }
    }
}