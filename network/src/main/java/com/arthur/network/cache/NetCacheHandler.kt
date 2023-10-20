package com.arthur.network.cache

import com.arthur.commonlib.utils.SPUtils
import com.arthur.commonlib.utils.json.JsonUtils
import com.arthur.network.Net
import java.lang.reflect.Type

/**
 *  * @Author Arthur
 *  * @Data 2023/4/26
 *  * @Description
 * ## 网络缓存处理
 */
object NetCacheHandler {

    const val GROUP_NET_CACHE = "SP_NET_CACHE"

    /**
     * 读取缓存
     */
    fun <R> read(key: String, type: Type): R? {
        val cacheJson = SPUtils.getData(key, defaultValue = "", group = GROUP_NET_CACHE)
        return convertData(cacheJson, type)
    }

    fun <R> read(key: String, clz: Class<R>): R? {
        return JsonUtils.fromJson(SPUtils.getData(key, defaultValue = "", group = GROUP_NET_CACHE), clz)
    }

    @Throws(Exception::class)
    private fun <R> convertData(cacheJson: String, type: Type): R? {
        return Net.client.netOptions.jsonConverter.converter(cacheJson, type)
    }


    /**
     * 缓存写入
     */
    fun write(key: String, response: String) {
        SPUtils.putData(key, response, GROUP_NET_CACHE)
    }
}