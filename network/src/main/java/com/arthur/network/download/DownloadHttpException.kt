package com.arthur.network.download

import okhttp3.Response

/**
 * 自定义okhttp 网络的错误
 */
class DownloadHttpException(private val response: Response) : Exception(getMessage(response)) {
    private val code: Int = response.code
    private val msg: String = response.message

    /**
     * HTTP status code.
     */
    fun code(): Int {
        return code
    }

    /**
     * HTTP status message.
     */
    fun message(): String {
        return msg
    }

    /**
     * The full HTTP response. This may be null if the exception was serialized.
     */
    fun response(): Response {
        return response
    }

    companion object {
        private fun getMessage(response: Response): String {
            return "HTTP " + response.code + " " + response.message
        }
    }
}