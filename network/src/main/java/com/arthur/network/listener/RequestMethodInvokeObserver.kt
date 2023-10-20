package com.arthur.network.listener

import okhttp3.Headers
import okhttp3.RequestBody
import java.lang.reflect.Type

/**
 *  * @Author Arthur
 *  * @Data 2023/5/23
 *  * @Description
 * ## 网络请求句柄调用监听
 */
interface RequestMethodInvokeObserver {

    /**
     * 网络请求句柄调用
     * @param url 回调出的接口名
     * @param responseType 此次请求要接收的数据类型
     * @param reqMethod 请求方式
     * @param requestBody 请求体类型
     */
    fun onMethodInvoke(
        url: String,
        responseType: Type,
        reqMethod: String,
        requestBody: RequestBody?,
        headers: Headers
    )
}