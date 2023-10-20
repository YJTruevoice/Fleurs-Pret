package com.arthur.network.model

/**
 * 异常类型
 */
enum class ErrorType(val type: Int) {
    /**
     * 自定义异常
     */
    CUSTOM_ERROR(100),

    /**
     * http异常
     */
    HTTP_ERROR(101),

    /**
     * 连接异常
     */
    CONNECT_ERROR(102),

    /**
     * json解析异常
     */
    PARSE_ERROR(103)
}