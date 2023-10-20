package com.arthur.network.callback

/**
 * 请求回调接口
 */
interface IHttpCallback<T> {
    fun onReqStart()
    fun onFinish()
    fun onSuccess(result: T)
    fun onError(t : Throwable?)
    fun onCache(result: T)
}