package com.arthur.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Author Arthur
 * @Data 2022/8/2
 * @Description
 * 通用的header添加拦截器
 */
class CommonHeadersInterceptor(private val commonHeaders: (() -> HashMap<String, String>) = { hashMapOf() }) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        for ((key, value) in commonHeaders.invoke().entries) {
            builder.header(key, value)
        }

        val response: Response
        try {
            response = chain.proceed(builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        return response
    }
}