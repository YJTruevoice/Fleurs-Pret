package com.arthur.network

import retrofit2.Retrofit

/**
 *  * @Author Arthur
 *  * @Data 2022/11/25
 *  * @Description
 *
 * ## 网络管理器
 *  * 获取retrofit对象
 *  * 创建网络请求service
 */
abstract class BaseNetMgr {

    /**
     * 网络请求配置
     */
    open val netOption: NetOptions = NetOptions()

    /**
     * 获取retrofit对象
     */
    open val retrofit: Retrofit by lazy {
        Net.client.createRetrofit(netOption)
    }

    /**
     * 直接根据T获取一个网络请求service
     */
    inline fun <reified T> service(): T {
        return retrofit.create(T::class.java)
    }

}