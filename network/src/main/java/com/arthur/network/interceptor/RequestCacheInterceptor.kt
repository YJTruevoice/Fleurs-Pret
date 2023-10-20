package com.arthur.network.interceptor

import com.arthur.commonlib.utils.StringUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 *  * @Author Arthur
 *  * @Data 2022/8/11
 *  * @Description
 * ## 请求缓存拦截器
 */
class RequestCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        //读接口上的@Headers里的注解配置
        var cacheControl = request.cacheControl.toString()

        //判断没有网络并且添加了@Headers注解,才使用网络缓存.
        if (!StringUtil.isBlank(cacheControl)) {
//            if (!NetUtil.hasNetwork(NCNetBiz.client.context)) {
//                //重置请求体;
//                request = request.newBuilder()
//                    //强制使用缓存
//                    .cacheControl(CacheControl.FORCE_CACHE)
//                    .build()
//
//            } else {
//                //如果有网络,则将缓存的过期时间,设置为0,获取最新数据
//                cacheControl = "public, max-age=0"
//            }
        } else {
            //如果没有添加注解,则不缓存
            //响应头设置成无缓存
            cacheControl = "no-store"
        }

        return chain.proceed(request).newBuilder()
            .header("Cache-Control", cacheControl)
            .removeHeader("pragma")// 移除了pragma消息头，移除它的原因是因为pragma也是控制缓存的一个消息头属性
            .build()
    }
}