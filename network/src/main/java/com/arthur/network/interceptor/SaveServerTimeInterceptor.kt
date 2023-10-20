package com.arthur.network.interceptor

import android.text.TextUtils
import com.arthur.network.Net
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.Exception

/**
 * @Author Arthur
 * @Data 2022/8/3
 * @Description
 * 保存服务器时间拦截器
 */
class SaveServerTimeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())

        val currentServerTime = response.headers.getDate("Date")?.time ?: 0L
        if (!TextUtils.isEmpty(currentServerTime.toString())) {
            try {
                Net.client.correctServerTimeGap(currentServerTime)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return response
    }

}