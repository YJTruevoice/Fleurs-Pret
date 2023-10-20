package com.arthur.network

/**
 * @Author Arthur
 * @Data 2022/8/3
 * @Description
 * 网络库使用到的常量
 */
object NetConstant {

    const val KEY_HOST = "KEY_HOST"

    const val KEY_REQUEST_METHOD = "request-method"
    const val VAL_BODY_REQUEST = "request-body"

    /**
     * 为了网络请求句柄都以表单形式展现，这里定义一个RequestBody请求标识header 在请求期间用于请求方式转换
     */
    const val BODY_REQUEST_METHOD_HEADER: String = "$KEY_REQUEST_METHOD:$VAL_BODY_REQUEST"

    /**
     * request body 请求编码方式
     */
    const val JSON_MEDIA_TYPE_ENCODE_METHOD = "application/json; charset=utf-8"

    /**
     * 网络错误代码
     */
    object ErrorCode {
        /**
         * 未知错误
         */
        const val ERROR_UNKNOWN = 2000

        /**
         * 数据解析异常
         */
        const val ERROR_PARSE = 2001

        /**
         * 网络超时
         */
        const val ERROR_NETWORK_TIMEOUT = 2002

        /**
         * http协议异常
         */
        const val ERROR_HTTP = 2003

        /**
         * 网络链接错误
         */
        const val ERROR_NETWORK_CONNECT = 2004

        /**
         * 网络连接错误-未知主机
         */
        const val ERROR_NETWORK_UNKNOWN_HOST = 2005

        /**
         * 网络连接错误-socket
         */
        const val ERROR_SOCKET = 2006

        /**
         * 网络连接错误-证书校验
         */
        const val ERROR_SSL = 2007

        /**
         * 数据不合规异常
         */
        const val ERROR_ILLEGAL_DATA = 1009
        const val ERROR_CACHE_CODE = 10001
    }

    /**
     * 对应HTTP的状态码
     */
    object HttpErrorCode {
        const val UNAUTHORIZED = 401
        const val BAD_REQUEST = 400
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val REQUEST_TIMEOUT = 408
        const val INTERNAL_SERVER_ERROR = 500
        const val BAD_GATEWAY = 502
        const val SERVICE_UNAVAILABLE = 503
        const val GATEWAY_TIMEOUT = 504
    }

    /**
     * 网络请求异常提示语
     */
    object ErrorPromptMsg {
        const val SERVICE_TIMEOUT = "网络超时，请稍后重试"
        const val CONNECT_ERROR = "网络不可用，请检查网络设置"
        const val CONNECT_ERROR_OTHER = "网络异常，请确认网络正常"
        const val DATA_PARSE_ERROR = "数据解析异常"
    }
}