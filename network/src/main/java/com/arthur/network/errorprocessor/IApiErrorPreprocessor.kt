package com.arthur.network.errorprocessor

import com.arthur.network.model.ErrorInfo

/**
 * @Author Arthur
 * @Data 2022/9/30
 * @Description
 * 网络请求异常处理接口
 */
interface IApiErrorPreprocessor {
    /**
     * @return 返回一个[ErrorInfo]
     */
    fun processError(error: Throwable?): ErrorInfo
}