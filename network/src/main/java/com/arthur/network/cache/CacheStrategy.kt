package com.arthur.network.cache

/**
 * @Author Arthur
 * @Data 2023/4/26
 * @Description
 * ## 缓存策略
 *  * cache only
 *  * cache first
 *  * cache force
 *  * online only
 */
enum class CacheStrategy {
    /**
     * 只读取使用缓存，线上数据写缓存
     */
    CACHE_ONLY,

    /**
     * 优先读取使用缓存，线上数据拉取到再覆盖，刷新UI
     */
    CACHE_FIRST,

    /**
     * 只拉取线上数据，不使用缓存
     */
    ONLINE_ONLY
}