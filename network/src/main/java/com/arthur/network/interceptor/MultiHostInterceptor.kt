package com.arthur.network.interceptor

import com.arthur.network.Net
import com.arthur.network.NetConstant
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author Arthur
 * @Data 2022/8/5
 * @Description
 * host拦截器
 */
class MultiHostInterceptor(val domainMap: ConcurrentHashMap<String, String>?, val backupDomain: String? = "") :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request().let { originalRequest ->
            val hosts = originalRequest.headers(NetConstant.KEY_HOST)
            if (hosts.isNotEmpty()) {
                originalRequest.newBuilder().let { builder ->
                    if (!Net.client.netOptions.isDebug) {
                        builder.removeHeader(NetConstant.KEY_HOST)
                    }
                    (domainMap?.get(hosts[0]) ?: backupDomain)?.toHttpUrlOrNull()
                        ?.let { baseUrl ->
                            //获取处理后的新newRequest
                            chain.proceed(
                                builder.url(
                                    originalRequest.url.newBuilder()
                                        .scheme(baseUrl.scheme) //http协议如：http或者https
                                        .host(baseUrl.host)     //主机地址
                                        .port(baseUrl.port)     //端口
                                        .build()
                                ).build()
                            )
                        } ?: chain.proceed(originalRequest)
                }
            } else {
                chain.proceed(originalRequest)
            }
        }
    }
}