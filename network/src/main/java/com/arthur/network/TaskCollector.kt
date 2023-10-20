package com.arthur.network

/**
 *  * @Author Arthur
 *  * @Data 2023/3/28
 *  * @Description
 * ## 任务收集器
 */
class TaskCollector {

    /**
     * 网络请求集合
     */
    private val requests: HashMap<String, suspend () -> Any?> = hashMapOf()

    /**
     * 加入一个网络请求
     */
    fun put(requestKey: String, request: suspend () -> Any?): TaskCollector {
        requests[requestKey] = request
        return this
    }

    /**
     * 获取网络请求集合
     */
    val values: HashMap<String, suspend () -> Any?> get() = requests
}